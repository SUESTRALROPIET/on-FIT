<template>
  <div>
    <div v-if="!loading" class="d-flex justify-center" id="loader">
      <!-- <self-building-square-spinner
        :animation-duration="6000"
        :size="40"
        color="#ff1d5e"
      /> -->
      <half-circle-spinner
        :animation-duration="1000"
        :size="60"
        color="#FFC46B"
      />
    </div>
    <div
      v-else
      class="d-flex flex-column mx-auto"
    >
      <v-row height="390px">
        <v-col cols="5" class="mr-5">
          <div class="d-flex align-center">
            <v-avatar
              class="mr-5 align-self-center"
              color="orange"
              size="60"
            >
              <span class="white--text text-h5">{{ userId[0] }}</span>
            </v-avatar>
            <div class="d-flex flex-column">
                <h1>{{ userId }} 님</h1>
            </div>
          </div>
          <div id="mypage-record"
            class="d-flex flex-column my-5"
          >
            <div class="d-flex justify-space-between">
              <h4>운동시간</h4>
              <h2>{{ this.exTimes }}</h2>
            </div>
            <div class="d-flex justify-space-between">
              <h4>칼로리</h4>
              <h2>{{ this.exCals }} kcal</h2>
            </div>
          </div>
        </v-col>
        <v-col class="ms-10" id="chart" cols="6">
          <apexchart
            type="line"
            height="350"
            v-if="showChart"
            :options="chartOptions"
            :series="series">
          </apexchart>
          <div v-else height="350"></div>
        </v-col>
      </v-row>
      <v-row id="mypage-bottom">
        <v-col cols="5">
          <!-- <Calendar @selected="selected" @current="current"/> -->
          <FunctionalCalendar
            ref="Calendar"
            v-model="calendar"
            v-if="showCal"
            :marked-dates='markedDates'
            :is-date-picker="true"
          />
        </v-col>
        <v-col  class="ms-10" cols="6" id="recordBox">
          <div v-if="this.calendar.selectedDate" class="text-center">
            <h2>{{this.calendar.selectedDate}} 운동 기록</h2>
          </div>
          <div v-if="showRecord">
            <CalendarDetail
              v-for="record in records"
              :key="record.exTime"
              :record="record"
            />
          </div>
          <div v-else>
            <h3 class="text-center mt-5">운동 기록이 없습니다.</h3>
          </div>
        </v-col>
        <!-- <v-col v-else class="ms-10" cols="6" id="recordBox">
          <h3 class="text-center mt-5">운동 기록이 없습니다.</h3>
        </v-col> -->
      </v-row>
    </div>
    <ButtonLogintoMyPage style="visibility: hidden;"/>
  </div>
</template>

<script>
import Vue from 'vue';
import Vuex, { mapGetters } from 'vuex';
// import axios from 'axios';
import _ from 'lodash';
import { apiInstance } from '@/api/index';
// import ButtonClubs from '@/views/mypage/components/ButtonClubs.vue';
// import ButtonNickname from '@/views/mypage/components/ButtonNickname.vue';
// import ButtonDeleteUser from '@/views/mypage/components/ButtonDeleteUser.vue';
// import ExRecord from '@/views/mypage/components/ExRecord.vue';
import CalendarDetail from '@/views/mypage/components/CalendarDetail.vue';
import { FunctionalCalendar } from 'vue-functional-calendar';
// import { SelfBuildingSquareSpinner } from 'epic-spinners';
import ButtonLogintoMyPage from '@/views/intro/components/ButtonLogintoMyPage.vue';
import { HalfCircleSpinner } from 'epic-spinners';

const userStore = 'userStore';
const api = apiInstance();

Vue.use(Vuex);

export default {
  name: 'MyPage',
  components: {
    CalendarDetail,
    FunctionalCalendar,
    HalfCircleSpinner,
    ButtonLogintoMyPage,
  },
  data() {
    return {
      loading: false,
      showCal: false,
      showChart: false,
      showRecord: false,
      exTimes: 0,
      exCals: 0,
      month: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
      len: [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],
      // cal
      calendar: {
      },
      markedDates: [],
      // chart
      series: [
        {
          name: '칼로리',
          type: 'line',
          data: [],
        },
        {
          name: '운동 시간',
          type: 'line',
          data: [],
        },
      ],
      chartOptions: {
        chart: {
          height: 350,
          type: 'line',
          zoom: {
            enabled: false,
          },
        },
        colors: ['#F4F10E', '#0E73F4', '#61D081'],
        stroke: {
          width: 3,
          curve: 'smooth',
        },
        xaxis: {
          categories: [],
        },
        yaxis: [{
          title: {
            text: '칼로리 (kcal)',
          },
        }, {
          opposite: true,
          title: {
            text: '운동 시간 (분)',
          },
        }],
      },
      records: [],
    };
  },
  created() {
    // this.$store.dispatch('getExDays');
    this.calendar.currentDate = new Date();
    this.getMonthlyLog();
    this.markedDates = this.$store.state.accountsStore.exDays;
    this.exTimes = this.$store.state.accountsStore.exTimes;
    this.exCals = this.$store.state.accountsStore.exCals;
    setTimeout(() => {
      this.loading = true;
      this.showCal = true;
    }, 500);
  },
  mounted() {
  },
  watch: {
    calendar: {
      deep: true,
      handler() {
        // eslint-disable-next-line no-unused-expressions
        this.yyyymmddSelected;
        // eslint-disable-next-line no-unused-expressions
        this.yyyymmCur;
        console.log('감지 되나?');
      },
    },
  },
  computed: {
    userId() {
      return this.$store.state.userStore.userId;
    },
    yyyymmddSelected() {
      const result = String(this.calendar.selectedDate).split('/');
      const mm = result[1].length === 2 ? result[1] : `0${result[1]}`;
      const dd = result[0].length === 2 ? result[0] : `0${result[0]}`;
      const yyyymmdd = `${result[2]}-${mm}-${dd}`;
      this.getRecords();
      return yyyymmdd;
    },
    yyyymmCur() {
      const result1 = String(this.calendar.currentDate).split(' ');
      const monthInt = this.month.indexOf(result1[1]) + 1;
      const monthStr = monthInt < 10 ? `0${monthInt}` : monthInt;
      const result2 = `${result1[3]}-${monthStr}`;
      this.getMonthlyLog();
      return result2;
    },
  },
  methods: {
    ...mapGetters(userStore, [
      'getUserId',
    ]),
    yyyymmdd(dateTime) {
      // const ddmmyyyy = this.selectedDate;
      const result = dateTime.split('/');
      const mm = result[1].length === 2 ? result[1] : `0${result[1]}`;
      const dd = result[0].length === 2 ? result[0] : `0${result[0]}`;
      const yyyymmdd = `${result[2]}-${mm}/${dd}`;
      return yyyymmdd;
    },
    ddmmyyyy(dateTime) {
      const result1 = dateTime.split('-');
      const result2 = result1.split('T');
      const ddmmyyyy = `${result2[0]}/${result1[1]}/${result1[0]}`;
      return ddmmyyyy;
    },
    getRecords() {
      this.showRecord = false;
      // axios.get(`http://localhost:8081/mypage/${userId}`)
      api.get(`/mypage/${this.getUserId()}`)
        .then((res) => {
          // 데이터 중 선택된 날짜만 필터링
          const result = res.data.MyExerciseLog;
          this.records = result.filter((v) => v.exTime.includes(this.yyyymmddSelected));
          setTimeout(() => { this.showRecord = true; }, 100);
        });
    },
    getMonthlyLog() {
      this.showChart = false;
      // const userId = this.getUserId();
      // axios.get(`http://localhost:8081/mypage/${userId}`)
      api.get(`/mypage/${this.getUserId()}`)
        .then((res) => {
          // eslint-disable-next-line max-len
          const monthlyLog = res.data.MyExerciseLog.filter((v) => v.exTime.includes(this.yyyymmCur));
          // 결과 저장할 배열
          const month = this.yyyymmCur.substr(5);
          const leng = this.len.at(month);
          this.series[0].data = new Array(leng).fill(0); // 칼로리
          this.series[1].data = new Array(leng).fill(0); // 시간
          this.chartOptions.xaxis.categories = _.range(1, leng + 1); // x축
          monthlyLog.forEach((v) => {
            const day = v.exTime.substr(8, 2) - 1;
            this.series[0].data[day] += v.exCal;
            this.series[1].data[day] += parseInt(v.exDuration / 60, 10);
          });
          setTimeout(() => { this.showChart = true; }, 100);
        });
    },
  },
};
</script>

<style scoped>
#mypage-record div{
  padding: 1rem 0rem;
}

#recordBox {
  background: rgb(255, 255, 255, 0.5);
  height: 350px;
  overflow: auto;
}

#loader {
  margin-top: 30vh;
}

#chart >>> .apexcharts-toolbar {
  visibility: hidden !important;
}
</style>
