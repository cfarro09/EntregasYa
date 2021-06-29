package com.delycomps.entregasya

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.webservice.Repository
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(context)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        val dialogLoading: AlertDialog = builderLoading.create()

        login_access.setOnClickListener {
            val username = login_username.text.toString()
            val password = login_password.text.toString()

            if (username != "" && password != "") {
                dialogLoading.show()
                Repository().login(username, password) { isSuccess, result, message ->
                    dialogLoading.hide()
                    if (isSuccess) {
                        SharedPrefsCache(requireContext()).set("firstname", result!!.firstName + " " + result.lastName, "string")
                        SharedPrefsCache(requireContext()).set("token", "Bearer ${result.token}", "string")
                        SharedPrefsCache(requireContext()).set("type", result.type, "string")
                        requireContext().startActivity(Intent(requireContext(), HomeActivity::class.java))
                    } else {
                        Snackbar.make(view, message as CharSequence, Snackbar.LENGTH_LONG).setBackgroundTint(resources.getColor(R.color.colorPrimary)).show()
                    }
                }
            }
        }

        login_register.setOnClickListener {
            (requireActivity().findViewById<View>(R.id.tabs) as TabLayout).getTabAt(1)!!.select()
        }
    }
}
