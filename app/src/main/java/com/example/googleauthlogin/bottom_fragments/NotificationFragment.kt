package com.example.googleauthlogin.bottom_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googleauthlogin.adapter.NotificationAdapter
import com.example.googleauthlogin.database.NotificationHelper
import com.example.googleauthlogin.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val view: View = binding.root

        notificationHelper = NotificationHelper()

        notificationHelper.getAllNotifications { list ->
            if (isAdded) { // Kiểm tra xem Fragment có được gắn kết không trước khi sử dụng context
                binding.rvNotification.layoutManager = GridLayoutManager(requireContext(), 1, LinearLayoutManager.VERTICAL, true)
                binding.rvNotification.setHasFixedSize(true)
                val adapt = NotificationAdapter(list)
                binding.rvNotification.adapter = adapt
            }
        }

        return view
    }
}
