package com.aaiztech.loanapp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.SharedPreferences

import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.*
import java.io.StringReader


class LoanOnMe : Fragment() {

    lateinit var recyclerLoanOnMe: RecyclerView


    var loanList: MutableList<LoanData> = arrayListOf()

    lateinit var myAdpater: LoanOnMeAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater!!.inflate(R.layout.fragment_loan_on_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabMyLoan: View = view.findViewById(R.id.fab_myLoan)
        recyclerLoanOnMe = view.findViewById(R.id.recycler_loanOnMe)
        recyclerLoanOnMe.hasFixedSize()
        recyclerLoanOnMe.layoutManager = LinearLayoutManager(requireContext())

        fabMyLoan.setOnClickListener {

            showDialog(requireActivity())
        }

        getAllData();


    }

    fun getAllData() {
        loanList.clear()
        try {
            var sharedPrefrence = requireActivity().getSharedPreferences("DATA", MODE_PRIVATE)
            var getLoanString = sharedPrefrence.getString("LoanOnMe", "Null")

            var index = 0
            val result = Klaxon().parseArray<LoanData>(getLoanString!!)
            while (index < result!!.size) {
                var lTitle = result[index].title
                var lAmount = result[index].amount
                loanList.add(LoanData(lTitle, lAmount))
                index++
            }
            myAdpater = LoanOnMeAdapter(requireContext(), loanList)
            recyclerLoanOnMe.adapter = myAdpater
        } catch (e: Exception) {
//            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    fun showDialog(activity: Activity) {

        val customDialog = Dialog(activity)

        customDialog.setCancelable(true)
        customDialog.setContentView(R.layout.dialoge_items)

        val edtTitle = customDialog.findViewById(R.id.edt_title) as EditText
        val edtAmount = customDialog.findViewById(R.id.edt_amount) as EditText
        val btnAdd = customDialog.findViewById(R.id.btn_addLoan) as Button
        btnAdd.setOnClickListener {

            saveData(edtTitle.text.toString(), edtAmount.text.toString())
            customDialog.cancel();
        }

        customDialog.show()

    }


    fun saveData(title: String, amount: String) {

        try {
            val preferences: SharedPreferences =
                requireContext().getSharedPreferences("DATA", MODE_PRIVATE)
            val editor = preferences.edit()

            val loanData = LoanData(title, amount)
            loanList.add(loanData)
            val result = Klaxon().toJsonString(loanList)
            editor.putString("LoanOnMe", result)
            editor.apply()

            var sharedPrefrence = requireActivity().getSharedPreferences("DATA", MODE_PRIVATE)
            var abc = sharedPrefrence.getString("LoanOnMe", "No Data")

            myAdpater.notifyDataSetChanged()
        } catch (e: Exception) {

            getAllData();
        }


    }

}


class LoanOnMeAdapter(val context: Context, val mList: MutableList<LoanData>) :
    RecyclerView.Adapter<LoanOnMeAdapter.ViewHolder>() {
    val preferences: SharedPreferences =
        context.getSharedPreferences("DATA", MODE_PRIVATE)
    val editor = preferences.edit()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.myloan_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        holder.txtMyLoanTitle.text = itemsViewModel.title
        holder.txtLoanAmount.text = itemsViewModel.amount
        holder.btnMyLoanDelete.setOnClickListener {
            mList.removeAt(position)

            notifyDataSetChanged()
            val result = Klaxon().toJsonString(mList)
            editor.putString("LoanOnMe", result)
            editor.apply()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txtMyLoanTitle: TextView = itemView.findViewById(R.id.txt_myLoanTitle)
        val txtLoanAmount: TextView = itemView.findViewById(R.id.txt_myLoanAmount)
        val btnMyLoanDelete: ImageButton = itemView.findViewById(R.id.btn_myLoanDelete)
    }
}


class LoanData(
    @Json(index = 1) val title: String,
    @Json(index = 2) val amount: String
)