package com.example.hostelpro.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.hostelpro.data.local.SeedDataInitializer
import com.example.hostelpro.domain.repository.AuthRepository
import com.example.hostelpro.presentation.common.theme.HostelProTheme
import com.example.hostelpro.presentation.navigation.HostelProNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var seedDataInitializer: SeedDataInitializer
    @Inject lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seedDataInitializer.seedIfNeeded()
        setContent {
            HostelProTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HostelProNavigation(authRepository = authRepository)
                }
            }
        }
    }
}
