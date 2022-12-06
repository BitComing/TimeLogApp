package com.bignerdranch.android.criminalintent.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.bean.Msg
import com.bignerdranch.android.criminalintent.bean.Todo
import java.util.*
import kotlin.collections.ArrayList

class TodoFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var todolist: List<Todo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =  inflater.inflate(R.layout.fragment_todo, container, false)
        recyclerView = view.findViewById(R.id.recyclerview_todo)

        recyclerView.layoutManager = LinearLayoutManager(this.activity)

        recyclerView.adapter = TodoAdapter(todolist)

        return view
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