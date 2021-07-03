package com.delycomps.entregasya.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.delycomps.entregasya.R
import com.delycomps.entregasya.cache.SharedPrefsCache
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var homeViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        profile_first_name.text = SharedPrefsCache(requireContext()).get("first_name", "string") as CharSequence?
        profile_last_name.text = SharedPrefsCache(requireContext()).get("last_name", "string") as CharSequence?
        profile_email.text = SharedPrefsCache(requireContext()).get("email", "string") as CharSequence?
        profile_document_type.text = SharedPrefsCache(requireContext()).get("doct_type", "string") as CharSequence?
        profile_document_number.text = SharedPrefsCache(requireContext()).get("doc_number", "string") as CharSequence?
        profile_address.text = SharedPrefsCache(requireContext()).get("", "string") as CharSequence?
        profile_phone.text = SharedPrefsCache(requireContext()).get("phone", "string") as CharSequence?
    }
}
