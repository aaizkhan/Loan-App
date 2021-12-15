package com.aaiztech.loanapp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon


class LoanOnOthers : Fragment() {

    lateinit var recyclerLoanOther: RecyclerView
   
    var otherLoanList: MutableList<LoanData> = arrayListOf()

    lateinit var myAdpater:OtherLoanAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_loan_on_others, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerLoanOther = view.findViewById(R.id.recycler_loanforOther)
        recyclerLoanOther.hasFixedSize()
        recyclerLoanOther.layoutManager = LinearLayoutManager(requireContext())

        val fabOtherLoan: View =view.findViewById(R.id.fab_myOtherLoan)
        fabOtherLoan.setOnClickListener {
            showDialog(requireActivity())
        }

        getAllData();
    }
    fun getAllData() {
        otherLoanList.clear()
        try {
            var sharedPrefrence = requireActivity().getSharedPreferences("DATA",
                Context.MODE_PRIVATE
            )
            var getLoanString = sharedPrefrence.getString("LoanforOther", "Null")

            var index = 0
            val result = Klaxon().parseArray<LoanData>(getLoanString!!)
            while (index < result!!.size) {
                var lTitle = result[index].title
                var lAmount = result[index].amount
                otherLoanList.add(LoanData(lTitle, lAmount))
                index++
            }
            myAdpater = OtherLoanAdapter(requireContext(), otherLoanList)
            recyclerLoanOther.adapter = myAdpater
        } catch (e: Exception) {
//            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    fun showDialog(activity: Activity) {

        val customDialog = Dialog(activity)
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(true)
        customDialog.setContentView(R.layout.dialoge_items)

        val edtTitle = customDialog.findViewById(R.id.edt_title) as EditText
        val edtAmount = customDialog.findViewById(R.id.edt_amount) as EditText
        val btnAdd = customDialog.findViewById(R.id.btn_addLoan) as Button
        btnAdd.setOnClickListener {
//            var getId = edtId.text as Int

            saveData(edtTitle.text.toString(), edtAmount.text.toString())
            customDialog.cancel();
        }

        customDialog.show()

    }

    fun saveData(title: String, amount: String) {
        try {
            val preferences: SharedPreferences =
                requireContext().getSharedPreferences("DATA", Context.MODE_PRIVATE)
            val editor = preferences.edit()

            val loanData = LoanData(title, amount)
            otherLoanList.add(loanData)
            val result = Klaxon().toJsonString(otherLoanList)
            editor.putString("LoanforOther", result)
            editor.apply()
//        getAllData()
//        Toast.makeText(requireContext(), "Data Added", Toast.LENGTH_SHORT).show()
            var sharedPrefrence = requireActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE)
            var abc = sharedPrefrence.getString("LoanforOther", "No Data")
//        Toast.makeText(context, abc.toString(), Toast.LENGTH_SHORT).show()

            myAdpater.notifyDataSetChanged()

        }catch (e:Exception){
            getAllData();
        }

    }


    class OtherLoanAdapter(val context: Context, val mList: MutableList<LoanData>) :
        RecyclerView.Adapter<OtherLoanAdapter.ViewHolder>() {
        val preferences: SharedPreferences =
            context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val editor = preferences.edit()

        // create new views
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // inflates the card_view_design view 
            // that is used to hold list item
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.myloan_item, parent, false)

            return ViewHolder(view)
        }

        // binds the list items to a view
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val itemsViewModel = mList[position]
            holder.txtMyLoanTitle.text = itemsViewModel.title
            holder.txtLoanAmount.text = itemsViewModel.amount
            holder.btnMyLoanDelete.setOnClickListener {
                mList.removeAt(position)

                notifyDataSetChanged()
                val result = Klaxon().toJsonString(mList)
                editor.putString("LoanforOther", result)
                editor.apply()
            }
        }

        // return the number of the items in the list
        override fun getItemCount(): Int {
            return mList.size
        }

        // Holds the views for adding it to image and text
        class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
            val txtMyLoanTitle: TextView = itemView.findViewById(R.id.txt_myLoanTitle)
            val txtLoanAmount: TextView = itemView.findViewById(R.id.txt_myLoanAmount)
            val btnMyLoanDelete: ImageButton = itemView.findViewById(R.id.btn_myLoanDelete)
        }
    }


}