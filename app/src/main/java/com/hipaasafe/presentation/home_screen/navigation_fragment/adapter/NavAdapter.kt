package com.hipaasafe.presentation.home_screen.navigation_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemNavDividerBinding
import com.hipaasafe.databinding.ItemNavMenuBinding
import com.hipaasafe.databinding.ItemNavProfileBinding
import com.hipaasafe.databinding.ItemNavTitleBinding
import com.hipaasafe.presentation.home_screen.navigation_fragment.model.NavItemType
import com.hipaasafe.presentation.home_screen.navigation_fragment.model.NavigationModel

class NavAdapter(
    val context: Context,
    private val navMenu: ArrayList<NavigationModel>,
    var listener: NavClickManager
) :
    RecyclerView.Adapter<NavAdapter.ViewHolder>() {
    class ViewHolder : RecyclerView.ViewHolder {
        var itemNavProfileBinding: ItemNavProfileBinding? = null
        var itemNavMenuBinding: ItemNavMenuBinding? = null
        var itemNavDividerBinding: ItemNavDividerBinding? = null
        var itemNavTitleBinding: ItemNavTitleBinding? = null

        constructor(binding: ItemNavMenuBinding) : super(binding.root) {
            itemNavMenuBinding = binding
        }

        constructor(binding: ItemNavProfileBinding) : super(binding.root) {
            itemNavProfileBinding = binding
        }

        constructor(binding: ItemNavDividerBinding) : super(binding.root) {
            itemNavDividerBinding = binding
        }

        constructor(binding: ItemNavTitleBinding) : super(binding.root) {
            itemNavTitleBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            NavItemType.ITEM_PROFILE.value -> {
                val v =
                    LayoutInflater.from(context).inflate(R.layout.item_nav_profile, parent, false)
                val binding = ItemNavProfileBinding.bind(v)
                ViewHolder(binding)
            }

            NavItemType.ITEM_MENU.value -> {
                val v = LayoutInflater.from(context).inflate(R.layout.item_nav_menu, parent, false)
                val binding = ItemNavMenuBinding.bind(v)
                ViewHolder(binding)
            }

            NavItemType.ITEM_SIGN_OUT.value -> {
                val v = LayoutInflater.from(context).inflate(R.layout.item_nav_title, parent, false)
                val binding = ItemNavTitleBinding.bind(v)
                ViewHolder(binding)
            }

            else -> {
                val v =
                    LayoutInflater.from(context).inflate(R.layout.item_nav_divider, parent, false)
                val binding = ItemNavDividerBinding.bind(v)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = navMenu[position]
        when (holder.itemViewType) {
            NavItemType.ITEM_PROFILE.value -> {
                holder.itemNavProfileBinding?.apply {
                    data.icon?.let { imgProfile.setImageResource(it) }
                    tvName.text = data.title
                    btnClose.setOnClickListener {
                        listener.onClickClose(position)
                    }
                    btnViewProfile.setOnClickListener {
                        listener.onClickViewProfile(position)
                    }
                }
            }
            NavItemType.ITEM_MENU.value -> {
                holder.itemNavMenuBinding?.apply {
                    data.icon?.let { imgMenu.setImageResource(it) }
                    tvMenu.text = data.title
                    holder.itemView.setOnClickListener {
                        listener.onClickMenu(position)
                    }
                }
            }
            NavItemType.ITEM_SIGN_OUT.value -> {
                holder.itemNavTitleBinding?.apply {
                    tvMenu.text = data.title
                    holder.itemView.setOnClickListener {
                        listener.onClickSignOut(position)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return navMenu.size
    }

    override fun getItemViewType(position: Int): Int {
        return navMenu[position].navItemType.value
    }

    interface NavClickManager {
        fun onClickClose(position: Int)
        fun onClickViewProfile(position: Int)
        fun onClickMenu(position: Int)
        fun onClickSignOut(position: Int)
    }

}