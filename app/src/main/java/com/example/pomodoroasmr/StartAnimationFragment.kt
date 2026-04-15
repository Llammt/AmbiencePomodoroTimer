package com.example.pomodoroasmr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.findNavController
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import com.example.pomodoroasmr.ui.PomodoroAppMainTheme

class StartAnimationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PomodoroAppMainTheme {
                    Surface {
                        view?.let {StartAnimationFragmentContent(it)}
                    }
                }
            }
        }
    }
}

@Composable
fun StartAnimationFragmentContent(view : View) {
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        SplashScreen(
            onAnimationFinished = {
                // Этот код выполнится, когда анимация закончится
                println("Анимация завершена!")
                view.findNavController()
                    .navigate(R.id.action_startAnimationFragment_to_selectSessionFragment)
            }
        )
    }
}

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(5000) // показываем анимацию 5 секунд
        onAnimationFinished() // лямбда, которую передаем в параметрах при вызове; конкретно в нашем случае, переходим на главный экран
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(2000) // 2 секунды на появление
        ) + scaleIn(
            initialScale = 0.5f,
            animationSpec = tween(2000)
        ),
        exit = fadeOut(
            animationSpec = tween(2000) //секунда на исчезновение
        )
    ) {
        // Красивый экран с логотипом
        Box(  // Контейнер
            modifier = Modifier.fillMaxSize().background(Color(0xFFAFBEA2)), // Растягивается на весь экран
            contentAlignment = Alignment.Center  // Всё внутри выравниваем по центру
        ) {
            Image(  // Картинка-логотип
                painter = painterResource(R.drawable.logo),  // Ресурс из папки res/drawable
                contentDescription = "Logo",  // Для accessibility (screen reader)
                modifier = Modifier.size(150.dp) // Ширина и высота 150 dp
            )
        }
    }
}


