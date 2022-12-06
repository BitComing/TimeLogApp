package com.bignerdranch.android.criminalintent.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.TimerActivity
import com.bignerdranch.android.criminalintent.bean.Msg
import com.bignerdranch.android.criminalintent.bean.Todo
import com.bignerdranch.android.criminalintent.viewmodel.CrimeListViewModel
import com.bignerdranch.android.criminalintent.viewmodel.TodoListViewModel
import java.util.*
import kotlin.collections.ArrayList

class TodoFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var todolist: List<Todo> = emptyList()

    private lateinit var btSend: Button
    private lateinit var editTodo: EditText

    private val todoListViewModel by lazy {
        ViewModelProvider(this).get(TodoListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_todo, container, false)
        btSend = view.findViewById(R.id.bt_send)
        editTodo = view.findViewById(R.id.edit_todo)

        recyclerView = view.findViewById(R.id.recyclerview_todo)
        val manager = LinearLayoutManager(this.activity)
        manager.stackFromEnd = true
        recyclerView.layoutManager = manager
        recyclerView.adapter = TodoAdapter(todolist)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btSend.setOnClickListener{
            val todoContent = editTodo.text.toString()
            val todo = Todo(UUID.randomUUID(), Date(), todoContent, false)
            todoListViewModel.addTodo(todo)
            editTodo.setText("")
        }


        todoListViewModel.noteListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { todos ->
                todos?.let {
                    recyclerView.adapter = TodoAdapter(it)
                }
            }
        )
    }

    private inner class TodoHolder(view: View): RecyclerView.ViewHolder(view) {
        private val textTodo = view.findViewById(R.id.text_todo) as TextView
        private val linearTodo = view.findViewById(R.id.linear_todo) as ConstraintLayout
        private val checkBox = view.findViewById<CheckBox>(R.id.checkbox)
        fun bind(todo: Todo) {
            textTodo.text = todo.content
            linearTodo.setOnClickListener{
                Intent(this@TodoFragment.context, TimerActivity::class.java).let {
                    it.putExtra("todo_content", todo.content)
                    it.putExtra("todo_id",todo.tid.toString())
                    startActivity(it)
                }
            }
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    todoListViewModel.deleteTodo(todo)
                }
            }
        }
    }

    private inner class TodoAdapter(var todos: List<Todo>):
        RecyclerView.Adapter<TodoHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : TodoHolder {
            val view = layoutInflater.inflate(R.layout.list_item_todo, parent, false)
            return TodoHolder(view)
        }
        override fun getItemCount() = todos.size

        override fun onBindViewHolder(holder: TodoHolder, position: Int) {
            val todo = todos[position]
            holder.bind(todo)
        }
    }

//    init {
//        todolist += Todo(UUID.randomUUID(), Date(),"test",false)
//        todolist += Todo(UUID.randomUUID(),Date(),"test",false)
//        todolist += Todo(UUID.randomUUID(),Date(),"test",false)
//        todolist += Todo(UUID.randomUUID(),Date(),"test",false)
//    }

}