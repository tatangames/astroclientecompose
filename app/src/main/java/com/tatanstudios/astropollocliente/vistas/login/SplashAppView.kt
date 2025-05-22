package com.tatanstudios.astropollocliente.vistas.login

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tatanstudios.astropollocliente.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.LottieConstants
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.vistas.opciones.carrito.CarritoScreen
import com.tatanstudios.astropollocliente.vistas.opciones.perfil.PerfilScreen
import com.tatanstudios.astropollocliente.vistas.principal.PrincipalScreen


class SplashApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // MODO VERTICAL
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        enableEdgeToEdge()
        setContent {
            // INICIO DE APLICACION
            AppNavigation()
        }
    }
}



// *** RUTAS DE NAVEGACION ***
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.VistaSplash.route) {

        composable(Routes.VistaSplash.route) { SplashScreen(navController) }
        composable(Routes.VistaLogin.route) { LoginScreen(navController) }
        composable(Routes.VistaRegistro.route) { RegistroScreen(navController) }
        composable(Routes.VistaRecuperarCorreo.route) { RecuperarPasswordScreen(navController) }

        composable(Routes.VistaCambiarPasswordCorreo.route) { backStackEntry ->
            val idusuario = backStackEntry.arguments?.getString("idusuario") ?: ""

            CambiarPasswordEmailScreen(navController = navController, idusuario = idusuario)
        }


        composable(Routes.VistaPrincipal.route) { PrincipalScreen(navController) }
        composable(Routes.VistaCarrito.route) { CarritoScreen(navController) }
        composable(Routes.VistaPerfil.route) { PerfilScreen(navController) }


    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    val ctx = LocalContext.current
    val tokenManager = remember { TokenManager(ctx) }

    // Ejecutar la migración desde SharedPreferences solo una vez
    LaunchedEffect(Unit) {
        tokenManager.migrateFromSharedPreferencesIfNeeded()
    }

    val idusuario by tokenManager.idUsuario.collectAsState(initial = "")

    // Evitar que el usuario vuelva al splash con el botón atrás
    DisposableEffect(Unit) {
        onDispose {
            navController.popBackStack(Routes.VistaSplash.route, true)
        }
    }

    // Control de la navegación tras un retraso
    LaunchedEffect(idusuario) {
        delay(3000)

        if (idusuario.isNotEmpty()) {
            navController.navigate(Routes.VistaPrincipal.route) {
                popUpTo(Routes.VistaSplash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.VistaLogin.route) {
                popUpTo(Routes.VistaSplash.route) { inclusive = true }
            }
        }
    }

    // Animación y diseño
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_hamburger))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFD84B4B)),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .height(225.dp)
                .align(Alignment.Center)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(bottom = 36.dp, start = 8.dp, end = 12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 22.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.hotpizza)),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(id = R.drawable.logopizza),
                contentDescription = stringResource(id = R.string.logotipo),
                modifier = Modifier
                    .size(width = 120.dp, height = 100.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    val navController = rememberNavController()
    SplashScreen(navController = navController)
}