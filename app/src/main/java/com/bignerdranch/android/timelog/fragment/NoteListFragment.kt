package com.bignerdranch.android.timelog.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.timelog.*
import com.bignerdranch.android.timelog.bean.Note
import com.bignerdranch.android.timelog.viewmodel.NoteListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_CRIME_ID = "crime_id"
private  const val TAG = "CrimeListFragment"
class CrimeListFragment : Fragment() {

//    interface Callbacks {
//        fun onCrimeSelected(crimeId: UUID)
//    }
//    private var callbacks: Callbacks? = null
    private lateinit var dayNoteRecyclerView: RecyclerView
    private var dayNoteAdapter: DayNoteAdapter? = DayNoteAdapter(emptyList())
    private lateinit var btnFloat : FloatingActionButton
    private lateinit var linearQuote: LinearLayout

    private val noteListViewModel: NoteListViewModel by lazy {
        ViewModelProvider(this).get(NoteListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
//        editSearch = view.findViewById(R.id.search_main)

        dayNoteRecyclerView = view.findViewById(R.id.crimeRecyclerView)
        dayNoteRecyclerView.layoutManager = LinearLayoutManager(context)
        dayNoteRecyclerView.adapter=dayNoteAdapter

        btnFloat = view.findViewById(R.id.btn_float)
        linearQuote = view.findViewById(R.id.linear_quote)

        return view
    }

    // 视图完成创建时，进行订阅监视
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteListViewModel.getAllDates().observe(
            viewLifecycleOwner,
            Observer { dates ->
                dates?.let {
                    updateDates(dates)
                }
            }
        )
        btnFloat.setOnClickListener{
            val note = Note()
            noteListViewModel.addCrime(note)
            val intent = Intent(this@CrimeListFragment.activity, NoteActivity::class.java)
            intent.putExtra(ARG_CRIME_ID,note.id)
            startActivity(intent)
        }
        btnFloat.setOnLongClickListener{
            val intent = Intent(this.activity, TimerActivity::class.java)
            startActivity(intent)
            false
        }
        linearQuote.setOnClickListener{
            Toast.makeText(this.context, "每日名言开发中", Toast.LENGTH_SHORT).show()
        }


    }
    private fun updateDates(dates: List<Date>) {
        var goodDates :MutableList<String> = ArrayList()
        val f1 = SimpleDateFormat("yyyy-MM-dd")
        for (i in dates) {
            goodDates+=f1.format(i)
        }

        dayNoteAdapter = DayNoteAdapter(goodDates.distinct())
        dayNoteRecyclerView.adapter = dayNoteAdapter
    }

    override fun onDetach() {
        super.onDetach()
//        callbacks = null
    }


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//
//        inflater.inflate(R.menu.fragment_crime_list, menu)
//        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
//        searchView.maxWidth = 900
//
//        searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//                val pattern = p0?.trim() as String
//                crimeList = crimeListViewModel.findCrime(pattern)
//                crimeList.observe(
//                    viewLifecycleOwner,
//                    Observer { crimes ->
//                        crimes?.let {
//                            updateUI(crimes)
//                        }
//                    }
//                )
//                return true
//            }
//        })
//    }


    // Note的RecyclerView
    private inner class NoteHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

        private lateinit var note: Note
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val txtStartTime: TextView = itemView.findViewById(R.id.text_start_time)
        private val txtEndTime: TextView = itemView.findViewById(R.id.text_end_time)
        private val durationTextView: TextView = itemView.findViewById(R.id.text_duration)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        fun bind(note: Note) {
            this.note = note
            titleTextView.text = this.note.title
            durationTextView.text = this.note.duration.toString()+"min"

            val tDate = this.note.date
            var hour = note.hour
            var min = note.min
            if (hour == 0 && min == 0) {
                hour = tDate.hours
                min = tDate.minutes
            }
            val start = min + hour * 60 - note.duration
            val startHour = start/60
            val startMin = start % 60

            if (startHour<10 && startMin >= 10) {
                txtStartTime.text = "0$startHour:$startMin"
            } else if (startHour >= 10 &&startMin<10) {
                txtStartTime.text = "$startHour:0$startMin"
            } else if (startHour<10 && startMin<10) {
                txtStartTime.text = "0$startHour:0$startMin"
            } else {
                txtStartTime.text = "$startHour:$startMin"
            }
            if (hour<10 && min>=10) {
                txtEndTime.text = "0$hour:$min"
            } else if (hour>=10 && min<10) {
                txtEndTime.text = "$hour:0$min"
            } else if (hour<10 && min<10) {
                txtEndTime.text = "0$hour:0$min"
            } else {
                txtEndTime.text = "$hour:$min"
            }
        }
        override fun onClick(v: View) {
            val intent = Intent(this@CrimeListFragment.activity, NoteActivity::class.java)
            intent.putExtra(ARG_CRIME_ID,note.id)
            startActivity(intent)
        }
        override fun onLongClick(p0: View?): Boolean {
            val popup = PopupMenu(this@CrimeListFragment.activity,p0)
            popup.menuInflater.inflate(R.menu.popup, popup.menu)
            popup.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_delete -> {
                        noteListViewModel.deleteNote(note)
                        Toast.makeText(activity, "删除成功", Toast.LENGTH_SHORT).show()
                    }
                    R.id.menu_copy -> {
                        Toast.makeText(activity, "复制成功", Toast.LENGTH_SHORT).show()
                    }
                    R.id.share -> {
                        val content = note.title
                        var startTime = ""
                        var endTime = ""

                        val tDate = this.note.date
                        var hour = note.hour
                        var min = note.min
                        if (hour == 0 && min == 0) {
                            hour = tDate.hours
                            min = tDate.minutes
                        }
                        val start = min + hour * 60 - note.duration
                        val startHour = start/60
                        val startMin = start % 60

                        if (startHour<10 && startMin >= 10) {
                            startTime = "0$startHour:$startMin"
                        } else if (startHour >= 10 &&startMin<10) {
                            startTime = "$startHour:0$startMin"
                        } else if (startHour<10 && startMin<10) {
                            startTime = "0$startHour:0$startMin"
                        } else {
                            startTime = "$startHour:$startMin"
                        }
                        if (hour<10 && min>=10) {
                            endTime = "0$hour:$min"
                        } else if (hour>=10 && min<10) {
                            endTime = "$hour:0$min"
                        } else if (hour<10 && startMin<10) {
                            endTime = "0$hour:0$min"
                        } else {
                            endTime = "$hour:$min"
                        }


                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, "$startTime-$endTime $content")
                        startActivity(Intent.createChooser(intent, "$startTime-$endTime"))
                        Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show()
                    }
                }
                false
            }
            popup.show()
            return false
        }
    }



    private inner class NoteAdapter(var notes: List<Note>) :
        RecyclerView.Adapter<NoteHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : NoteHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return NoteHolder(view)
        }
        override fun getItemCount() = notes.size

        override fun onBindViewHolder(holder: NoteHolder, position: Int) {
            val crime = notes[position]
            holder.bind(crime)
        }
    }


    private inner class DayNoteHolder(view: View):RecyclerView.ViewHolder(view) {
        val textDay: TextView = itemView.findViewById(R.id.text_day)
        val linearDay: LinearLayout = itemView.findViewById(R.id.linear_day)
        val txtTotalTime: TextView = itemView.findViewById(R.id.txt_total_time)
        var recyclerView: RecyclerView = itemView.findViewById(R.id.recycler_view_day)
    }
    private inner class DayNoteAdapter(var dates: List<String>): RecyclerView.Adapter<DayNoteHolder>(){
        override fun getItemCount() = dates.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayNoteHolder {
            val view = layoutInflater.inflate(R.layout.list_item_daynote,parent,false)
            return DayNoteHolder(view)
        }
        override fun onBindViewHolder(holder: DayNoteHolder, position: Int) {
            val date = dates[position]
            val notes: MutableList<Note> = ArrayList()

            holder.textDay.text = date
            holder.linearDay.setOnClickListener {
                when(holder.recyclerView.visibility) {
                    View.VISIBLE -> holder.recyclerView.visibility=View.GONE
                    else -> holder.recyclerView.visibility=View.VISIBLE
                }
            }
            holder.recyclerView.layoutManager= LinearLayoutManager(context)
            holder.recyclerView.adapter=NoteAdapter(notes)
            noteListViewModel.crimeListLiveData.observe(
                viewLifecycleOwner,
                Observer { notes ->
                    notes?.let {
                        var totalTime = 0
                        val f1 = SimpleDateFormat("yyyy-MM-dd")
                        val rightNotes : MutableList<Note> = ArrayList()
                        for (i in it) {
                            if (f1.format(i.date).toString() == date) {
                                rightNotes+=i
                                totalTime+=i.duration
                            }
                        }
                        rightNotes.sortByDescending  { T -> (T.hour*60 + T.min) }
                        holder.recyclerView.adapter=NoteAdapter(rightNotes)
                        if (totalTime > 60) {
                            val hour = totalTime/60
                            val min = totalTime%60
                            holder.txtTotalTime.text = "本日已记录时间：$hour"+"h"+"$min"+"min"
                        } else{
                            holder.txtTotalTime.text = "本日已记录时间：$totalTime"+"min"
                        }
                    }
                }
            )

        }
    }
}