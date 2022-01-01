package com.example.appofmanydecks.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Collects a flow as state, and with lifecycle awareness. The flow collection will only happen when
 * the state of [lifecycleOwner] is [lifecycleState] or higher. This prevents unnecessary background
 * work of cold flows, by cancelling the flow when it isn't needed.
 */
@Composable
fun <T> Flow<T>.collectAsStateWithLifecycle(
    initial: T,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
) = remember(this, lifecycleOwner) {
    this.flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
}.collectAsState(initial)
