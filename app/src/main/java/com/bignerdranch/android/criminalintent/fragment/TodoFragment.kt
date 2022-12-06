package com.bignerdranch.android.criminalintent.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.R
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
        var view =  inflater.inflate(R.layout.fragment_todo, container, false)
        btSend = view.findViewById(R.id.bt_send)
        editTodo = view.findViewById(R.id.edit_todo)

        recyclerView = view.findViewById(R.id.recyclerview_todo)

        recyclerView.layoutManager = LinearLayoutManager(this.activity)

        recyclerView.adapter = TodoAdapter(todolist)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btSend.setOnClickListener{
            val todoContent = editTodo.text.toString()
            val todo = Todo(UUID.randomUUID(), Date(), todoContent, false)
            todoListViewModel.addTodo(todo)
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
        fun bind(todo: Todo) {
            textTodo.text = todo.content
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

    init {
        todolist += Todo(UUID.randomUUID(), Date(),"test",false)
        todolist += Todo(UUID.randomUUID(),Date(),"test",false)
        todolist += Todo(UUID.randomUUID(),Date(),"test",false)
        todolist += Todo(UUID.randomUUID(),Date(),"test",false)
    }

}