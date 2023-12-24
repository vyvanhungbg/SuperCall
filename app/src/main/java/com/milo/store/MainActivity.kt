package com.milo.store

import android.view.View
import android.widget.PopupMenu
import androidx.navigation.findNavController
import com.android.milo_store.base.BaseActivity
import com.milo.store.databinding.ActivityMainBinding
import com.milo.store.utils.extension.color
import com.milo.store.utils.extension.hide
import com.milo.store.utils.extension.show

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun initData() {

    }

    override fun setOnClick() {

    }

    override fun bindData() {
        setUpBottomNavigation()
    }


    private fun setUpBottomNavigation() {

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav_menu)
        binding.navView.setupWithNavController(popupMenu.menu, navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.contactFragment, R.id.historyCallFragment, R.id.favoriteContactFragment, R.id.numPadFragment, R.id.settingFragment -> binding.navView.show()
                else -> binding.navView.hide()
            }
        }
        // binding.navView.isVisible = false
        setUpHideStatusBar()
        // hideSystemUi()
    }

    private fun setUpHideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = color(R.color._F5F5F5)
    }
}