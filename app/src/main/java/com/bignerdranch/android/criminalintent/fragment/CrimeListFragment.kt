package com.bignerdranch.android.criminalintent.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.*
import com.bignerdranch.android.criminalintent.bean.DayNote
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

private const val ARG_CRIME_ID = "crime_id"
private  const val TAG = "CrimeListFragment"
class CrimeListFragment : Fragment() {

    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }
    private var callbacks: Callbacks? = null

//    private lateinit var crimeRecyclerView: RecyclerView
//    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())
    private lateinit var dayNoteRecyclerView: RecyclerView
    private var dayNoteAdapter: DayNoteAdapter? = DayNoteAdapter(emptyList())

//    private lateinit var crimeList: LiveData<List<Crime>>
//    private lateinit var dateList : LiveData<List<Date>>

//    private lateinit var editSearch: EditText
    private lateinit var btnFloat : FloatingActionButton

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    // fragment和activity相关联
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

//        crimeRecyclerView = view.findViewById(R.id.crimeRecyclerView) as RecyclerView
//        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
//        crimeRecyclerView.adapter=adapter

//        editSearch = view.findViewById(R.id.search_main)

        dayNoteRecyclerView = view.findViewById(R.id.crimeRecyclerView)
        dayNoteRecyclerView.layoutManager = LinearLayoutManager(context)
        dayNoteRecyclerView.adapter=dayNoteAdapter

        btnFloat = view.findViewById(R.id.btn_float)
        return view
    }

    // 视图完成创建时，进行订阅监视
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        // 待用
//        crimeListViewModel.crimeListLiveData.observe(
//            viewLifecycleOwner,
//            Observer { crimes ->
//                crimes?.let {
//                    updateUI(crimes)
//                }
//            }
//        )
        crimeListViewModel.getAllDates().observe(
            viewLifecycleOwner,
            Observer { dates ->
                dates?.let {
                    updateDates(dates)
                }
            }
        )
        btnFloat.setOnClickListener{
            val crime = Crime()
            crimeListViewModel.addCrime(crime)
            val intent = Intent(this@CrimeListFragment.activity, CrimeActivity::class.java)
            intent.putExtra(ARG_CRIME_ID,crime.id)
            startActivity(intent)
//            callbacks?.onCrimeSelected(crime.id)
//            true
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
//    private fun updateUI(crimes: List<Crime>) {
//        adapter = CrimeAdapter(crimes)
//        crimeRecyclerView.adapter = adapter
//    }


    override fun onDetach() {
        super.onDetach()
        callbacks = null
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

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when(item.itemId){
//            R.id.new_crime->{
//                val crime = Crime()
//                crimeListViewModel.addCrime(crime)
//                callbacks?.onCrimeSelected(crime.id)
//                true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }


//    companion object {
//
//        fun newInstance(): CrimeListFragment {
//            return CrimeListFragment()
//        }
//    }

    // Note的RecyclerView
    private inner class CrimeHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

        private lateinit var crime: Crime
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val txtStartTime: TextView = itemView.findViewById(R.id.text_start_time)
        private val txtEndTime: TextView = itemView.findViewById(R.id.text_end_time)
        private val durationTextView: TextView = itemView.findViewById(R.id.text_duration)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            durationTextView.text = this.crime.duration.toString()+"min"
//            val f1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            val f1 = SimpleDateFormat("HH:mm")
            val date = this.crime.date
            val startTime = Date(date.time - crime.duration * 60 * 1000)
            txtStartTime.text = f1.format(startTime)
            txtEndTime.text = f1.format(date)
        }
        override fun onClick(v: View) {
//            callbacks?.onCrimeSelected(crime.id)
            val intent = Intent(this@CrimeListFragment.activity, CrimeActivity::class.java)
            intent.putExtra(ARG_CRIME_ID,crime.id)
            startActivity(intent)
        }
        override fun onLongClick(p0: View?): Boolean {
            val popup = PopupMenu(this@CrimeListFragment.activity,p0)
            popup.menuInflater.inflate(R.menu.popup, popup.menu)
            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.menu_delete) {

                    Toast.makeText(activity, "Yes", Toast.LENGTH_SHORT).show()
                }
                if (it.itemId == R.id.two) {
                    Toast.makeText(activity, "Yes", Toast.LENGTH_SHORT).show()
                }
                false
            }
            popup.show()
            return false
        }
    }

    private inner class  CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }
        override fun getItemCount() = crimes.size

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }
    }

    // DayNote的RecyclerView
    private inner class DayNoteHolder(view: View):RecyclerView.ViewHolder(view) {
        val textDay: TextView = itemView.findViewById(R.id.text_day)
        val textFold: TextView = itemView.findViewById(R.id.txt_fold)
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
            val crimes: MutableList<Crime> = ArrayList()

            holder.textDay.text = date
            holder.textFold.setOnClickListener {
                when(holder.recyclerView.visibility) {
                    View.VISIBLE -> holder.recyclerView.visibility=View.GONE
                    else -> holder.recyclerView.visibility=View.VISIBLE
                }
            }

            holder.recyclerView.layoutManager= LinearLayoutManager(context)
            holder.recyclerView.adapter=CrimeAdapter(crimes)
            crimeListViewModel.crimeListLiveData.observe(
                viewLifecycleOwner,
                Observer { notes ->
                    notes?.let {
                        // 可优化成数据库搜索
                        var totalTime = 0
                        val f1 = SimpleDateFormat("yyyy-MM-dd")
                        val rightNotes : MutableList<Crime> = ArrayList()
                        for (i in it) {
                            if (f1.format(i.date).toString() == date) {
                                rightNotes+=i
                                totalTime+=i.duration
                            }
                        }
                        holder.recyclerView.adapter=CrimeAdapter(rightNotes)
                        holder.txtTotalTime.text = "本日已记录时间：$totalTime"+"mins"
                    }
                }
            )

        }
    }
}