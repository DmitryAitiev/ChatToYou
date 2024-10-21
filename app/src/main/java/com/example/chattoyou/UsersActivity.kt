package com.example.chattoyou

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.chattoyou.databinding.ActivityUsersBinding
import com.google.android.material.navigation.NavigationView

class UsersActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityUsersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navigationView

        // Устанавливаем кастомный Toolbar как ActionBar
        setSupportActionBar(binding.toolbar)

        // Настройка гамбургер-кнопки для бокового меню
        toggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Обработка нажатий на элементы бокового меню
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(LoginActivity.newIntent(this@UsersActivity))
                }
            }
            drawerLayout.closeDrawers() // Закрыть меню после нажатия
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Обрабатываем нажатие на кнопку меню (гамбургер)
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    companion object {
        fun newIntent(context: Context):Intent {
            return Intent(context, UsersActivity::class.java)
        }
    }
}