/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

/**
 * Simple App Example with plain javascript "as-is"
 *
 * @link http://vuejs.org/guide/index.html#All_Together_Now
 * @link http://vuejs.org/guide/components.html
 */
(function () {

  // Your component goes here...
  var MyComponent = Vue.extend({
    template: '<div> \
        <input v-model="newTodo" v-on:keyup.enter="addTodo"> \
            <ul> \
            <li v-for="todo in todos"> \
            <span>{{ todo.text }}</span> \
            <button v-on:click="removeTodo($index)">X</button> \
            </li> \
            </ul> \
            </div>',

    data: function () {
      return {
        newTodo: '',
        todos: [
          {text: 'Add some todos'}
        ]
      }
    },
    methods: {
      addTodo: function () {
        var text = this.newTodo.trim()
        if (text) {
          this.todos.push({ text: text })
          this.newTodo = ''
        }
      },
      removeTodo: function (index) {
        this.todos.splice(index, 1)
      }
    }
  })

  // Register the component
  Vue.component('my-component', MyComponent)

  // Kick-off the vue.js app
  new Vue({
    el: '#example'
  })

})()
