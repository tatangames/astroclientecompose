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
import com.tatanstudios.astropollocliente.vistas.principal.opciones.perfil.PerfilScreen
import com.tatanstudios.astropollocliente.vistas.principal.opciones.historial.HistorialFechaScreen
import com.tatanstudios.astropollocliente.vistas.principal.opciones.historial.HistorialOrdenScreen
import com.tatanstudios.astropollocliente.vistas.principal.opciones.historial.InfoProductoHistorialScreen
import com.tatanstudios.astropollocliente.vistas.principal.opciones.historial.ListadoProductosHistorialScreen
import com.tatanstudios.astropollocliente.vistas.principal.PrincipalScreen
import com.tatanstudios.astropollocliente.vistas.principal.opciones.direcciones.MapaScreen
import com.tatanstudios.astropollocliente.vistas.principal.opciones.direcciones.MisDireccionesScreen
import com.tatanstudios.astropollocliente.vistas.principal.opciones.direcciones.RegistrarNuevaDireccionScreen
import com.tatanstudios.astropollocliente.vistas.principal.opciones.direcciones.SeleccionarDireccionScreen
import com.tatanstudios.astropollocliente.vistas.principal.opciones.horarios.HorariosScreen
import com.tatanstudios.astropollocliente.vistas.principal.opciones.password.ActualizarPasswordScreen
import com.tatanstudios.astropollocliente.vistas.principal.opciones.premios.PremiosScreen


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
        composable(Routes.VistaHistorialFecha.route) { HistorialFechaScreen(navController) }

        // HISTORIAL LISTADO ORDENES
        composable(Routes.VistaHistorialListadoOrden.route) { backStackEntry ->
            val fecha1 = backStackEntry.arguments?.getString("fecha1") ?: ""
            val fecha2 = backStackEntry.arguments?.getString("fecha2") ?: ""

            HistorialOrdenScreen(navController = navController, _fecha1 = fecha1, _fecha2 = fecha2)
        }

        // LISTADO DE PRODUCTOS HISTORIAL ORDEN
        composable(Routes.VistaListadoProductosHistorialOrden.route) { backStackEntry ->
            val idordenStr = backStackEntry.arguments?.getString("idorden") ?: "0"
            val idorden = idordenStr.toIntOrNull() ?: 0

            ListadoProductosHistorialScreen(navController = navController, _idorden = idorden)
        }

        // VISTA INFO PRODUCTO DE HISTORIAL
        composable(Routes.VistaInfoProductoHistorialOrden.route) { backStackEntry ->
            val idproductoStr = backStackEntry.arguments?.getString("idproducto") ?: "0"
            val idproducto = idproductoStr.toIntOrNull() ?: 0

            InfoProductoHistorialScreen(navController = navController, _idproducto = idproducto)
        }

        // VISTA ACTUALIZAR CONTRASENA
        composable(Routes.VistaActualizarContrasena.route) { ActualizarPasswordScreen(navController) }
        // VISTA HORARIOS
        composable(Routes.VistaHorarios.route) { HorariosScreen(navController) }
        // VISTA PREMIOS
        composable(Routes.VistaPremios.route) { PremiosScreen(navController) }
        // VISTA MIS DIRECCIONES
        composable(Routes.VistaMisDirecciones.route) { MisDireccionesScreen(navController) }
        // VISTA MAPA
        composable(Routes.VistaMapa.route) { MapaScreen(navController) }

        // VISTA REGISTRAR NUEVA DIRECCION
        composable(Routes.VistaRegistroDireccion.route) { backStackEntry ->

            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            val latitud = backStackEntry.arguments?.getString("latitud") ?: "0"
            val longitud = backStackEntry.arguments?.getString("longitud") ?: "0"
            val latitudrealStr = backStackEntry.arguments?.getString("latitudreal")
            val longitudrealStr = backStackEntry.arguments?.getString("longitudreal")

            val latitudreal = if (latitudrealStr == "none") null else latitudrealStr
            val longitudreal = if (longitudrealStr == "none") null else longitudrealStr

            RegistrarNuevaDireccionScreen(
                navController = navController,
                idzona = id,
                latitud = latitud,
                longitud = longitud,
                latitudreal = latitudreal,
                longitudreal = longitudreal
            )
        }

        // VISTA SELECCIONAR DIRECCION
        composable(Routes.VistaSeleccionarDireccion.route) { backStackEntry ->

            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
            val direccion = backStackEntry.arguments?.getString("direccion") ?: ""
            val referencia = backStackEntry.arguments?.getString("referencia") ?: ""

            SeleccionarDireccionScreen(
                navController = navController,
                id = id,
                nombre,
                telefono,
                direccion,
                referencia
            )
        }


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