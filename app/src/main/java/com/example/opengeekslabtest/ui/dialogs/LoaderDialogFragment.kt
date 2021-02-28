package com.example.opengeekslabtest.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.opengeekslabtest.R

class LoaderDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogLoader)
        val view = requireActivity().layoutInflater.inflate(R.layout.layout_dialog_loader, null)
        alertDialogBuilder.setView(view)
        return alertDialogBuilder.create()
    }
}