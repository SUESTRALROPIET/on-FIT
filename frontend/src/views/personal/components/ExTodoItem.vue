<template>
  <div class="d-flex">
    <v-col class="exBox d-flex align-center" cols="7">
      <img :src="require(`@/assets/exercise/${exTodo.todoEng}.png`)" height="55px">
      <h4 class="pl-4">{{ exTodo.todoName }} ({{ exTodo.numShow }})</h4>
    </v-col>
    <v-col class="setBox d-flex align-center justify-center" cols="3">
      <h4>{{ exTodo.todoSet }} set</h4>
    </v-col>
    <v-btn
      class="mt-5 ml-4"
      fab
      dark
      x-small
      color="red"
      @click="deleteTodo"
    >
      <v-icon>mdi-minus</v-icon>
    </v-btn>
  </div>
</template>

<script>
export default {
  name: 'ExTodoItem',
  props: {
    exTodo: {
      type: Object,
      required: true,
    },
  },
  methods: {
    deleteTodo() {
      this.$emit('delete-todo', this.exTodo);
    },
  },
};
</script>

<style scoped>
.exBox {
  margin-left: 10px;
  height: 77px;
  /* border: 1px solid; */
  background: #ffd2b625;
  border-radius: 30px 0 0 30px;
  padding: 0;
  margin-bottom: 30px;
}

.setBox {
  height: 77px;
  /* border: 1px solid; */
  background: #FF8F4A25;
  border-radius: 0 30px 30px 0;
  padding: 0;
  margin-bottom: 30px;
}
</style>
