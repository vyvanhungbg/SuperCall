package com.milo.store.presentation.setting

import android.app.role.RoleManager
import android.content.Intent
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.milo_store.base.BaseFragment
import com.milo.store.BuildConfig
import com.milo.store.R
import com.milo.store.call.extension.isQPlus
import com.milo.store.databinding.FragmentSettingBinding
import com.milo.store.utils.extension.checkIsAppCallDefault
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val TAG = "SettingFragment"

class SettingFragment :
    BaseFragment<FragmentSettingBinding, SettingNavigation>() {

    override fun getLayoutId() = R.layout.fragment_setting

    override val viewModel by viewModel<SettingViewModel>()
    override val navigation: SettingNavigation
        get() = SettingNavigation(this)

    private val resultDefaultDialerIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            context?.let {
                if (it.checkIsAppCallDefault()) {
                    Toast.makeText(
                        activity?.applicationContext, "Is dialer1", Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        activity?.applicationContext, "Permission denied2", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    private val resultDefaultQPlush =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

    private fun launchSetDefaultDialerIntent() {
        if (isQPlus()) {
            val roleManager = context?.getSystemService(RoleManager::class.java)
            if (roleManager!!.isRoleAvailable(RoleManager.ROLE_DIALER) && !roleManager.isRoleHeld(
                    RoleManager.ROLE_DIALER
                )
            ) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                resultDefaultDialerIntent.launch(intent)
            } else {
                Toast.makeText(
                    activity?.applicationContext, "Is dialer3", Toast.LENGTH_LONG
                ).show()
            }
        } else {
            if (context?.getSystemService(TelecomManager::class.java)?.defaultDialerPackage != BuildConfig.APPLICATION_ID) {
                try {
                    Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).putExtra(
                        TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                        BuildConfig.APPLICATION_ID
                    ).apply {
                        resultDefaultDialerIntent.launch(this)
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        activity?.applicationContext, "Permission denied4", Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    activity?.applicationContext, "Is dialer5", Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    fun setDefaultCallerIdApp() {
        if(isQPlus()){
            val roleManager = context?.getSystemService(RoleManager::class.java)
            roleManager?.let {
                if (roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) && !roleManager.isRoleHeld(
                        RoleManager.ROLE_CALL_SCREENING
                    )
                ) {
                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
                    resultDefaultQPlush.launch(intent)
                }
            }
        }
    }

    override fun initData() {

    }

    override fun observeData() {

    }

    override fun setView() {
        setDefaultCallerIdApp()
        launchSetDefaultDialerIntent()
    }

    override fun setOnClick() {

    }


}