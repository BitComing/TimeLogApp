package com.bignerdranch.android.criminalintent

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private  const val TAG = "CrimeListFragment"
class CrimeListFragment : Fragment() {

    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    // 理解回调
    private var callbacks: Callbacks? = null
    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())
    private lateinit var crimeList: LiveData<List<Crime>>
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
        setHasOptionsMenu(true)
    }

    // 每次调用该生命周期，视图都要进行数据更新
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crimeRecyclerView) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter=adapter

        btnFloat = view.findViewById(R.id.btn_float)
        return view
    }

    // 视图完成创建时，进行订阅监视
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    updateUI(crimes)
                }
            }
        )
        btnFloat.setOnClickListener{
            val crime = Crime()
            crimeListViewModel.addCrime(crime)
            callbacks?.onCrimeSelected(crime.id)
            true
        }

    }
    private fun updateUI(crimes: List<Crime>) {
        // 更新crimeListViewModel临时存储的数据，并实例化CrimeAdapter
        adapter = CrimeAdapter(crimes)

        // 更新crimeRecyclerView的adapter
        crimeRecyclerView.adapter = adapter
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)

        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.maxWidth = 900

        searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val pattern = p0?.trim() as String
                crimeList = crimeListViewModel.findCrime(pattern)
                crimeList.observe(
                    viewLifecycleOwner,
                    Observer { crimes ->
                        crimes?.let {
                            updateUI(crimes)
                        }
                    }
                )
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.new_crime->{
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CrimeListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1,param1)
                    putString(ARG_PARAM2,param2)
                }
            }
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }



    //每一View的数据装载到CrimeHolder中
    private inner class CrimeHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

        private lateinit var crime: Crime
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)

        init {
            itemView.setOnClickListener(this)
        }

        // 定义数据绑定方法
        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
//            val f1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            val f1 = SimpleDateFormat("HH:mm:ss")
            val date = this.crime.date
            dateTextView.text = f1.format(date)
        }
        // 点击viewholder的反馈
        override fun onClick(v: View) {
            callbacks?.onCrimeSelected(crime.id)
        }

        override fun onLongClick(p0: View?): Boolean {
            // 弹不出菜单
            val popup = PopupMenu(this@CrimeListFragment.context,p0)
            popup.menuInflater.inflate(R.menu.popup, popup.menu)
            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.menu_delete) {
                    Toast.makeText(activity, "Yes", Toast.LENGTH_SHORT).show()
                }
                if (it.itemId == R.id.two) {
                    Toast.makeText(activity, "Yes", Toast.LENGTH_SHORT).show()
                }
                true
            }
            popup.show()
            return true
        }
    }

    // CrimeAdapter处理CrimeHolder对象
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


}