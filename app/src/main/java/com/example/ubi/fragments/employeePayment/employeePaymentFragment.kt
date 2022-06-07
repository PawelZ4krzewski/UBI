package com.example.ubi.fragments.employeePayment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.example.ubi.R
import com.example.ubi.activities.MainViewModel
import com.example.ubi.database.PPKDatabase
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.databinding.FragmentEmployeePaymentBinding
import com.example.ubi.fragments.homeScreen.HomeScreenViewModel
import java.text.SimpleDateFormat
import java.util.*


class employeePaymentFragment : Fragment() {

    private val viewModel by lazy{
        val application = requireNotNull(this.activity).application
        val dao = PPKDatabase.getDatabase(application).PaymentDao()
        val repository = PaymentRepository(dao)
        employeePaymentViewModel(repository,application)
    }

    private val mainViewModel: MainViewModel by activityViewModels()


    private var _binding: FragmentEmployeePaymentBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeePaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.extEmpPerTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setOwnPayment(text.toString())
        }

        binding.companyNameTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setEmpPayment(text.toString())
        }

        binding.dateInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setDate(text.toString())
        }

        binding.unitValuetTextInputEditText.text = mainViewModel.ppk.values[mainViewModel.ppk.values.size-1]

        setupDatePicker()

    }

    private fun setupDatePicker(){

        val tvdatePicker = binding.dateInputEditText

        val calendar = Calendar.getInstance()


        val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            updateLabel(calendar)
        }

        tvdatePicker.setOnClickListener {
            DatePickerDialog(requireContext(), datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
    private fun updateLabel(calendar: Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
        binding.dateInputEditText.setText(sdf.format(calendar.time))
    }


}