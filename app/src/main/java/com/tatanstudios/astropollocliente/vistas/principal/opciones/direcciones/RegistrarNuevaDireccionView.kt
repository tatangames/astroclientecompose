package com.tatanstudios.astropollocliente.vistas.principal.opciones.direcciones

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BloqueEntradaGeneral
import com.tatanstudios.astropollocliente.componentes.CustomModal1Boton
import com.tatanstudios.astropollocliente.componentes.CustomModal2Botones
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.viewmodel.RegistroNuevaDireccionViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun RegistrarNuevaDireccionScreen(navController: NavHostController,
                                  idzona: Int, latitud: String, longitud: String,
                                  latitudreal: String?, longitudreal: String?,
                                  viewModel: RegistroNuevaDireccionViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val resultado by viewModel.resultado.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }


    val nombre by viewModel.nombre.observeAsState("")
    val telefono by viewModel.telefono.observeAsState("")
    val puntoReferencia by viewModel.puntoReferencia.observeAsState("")
    val direccion by viewModel.direccion.observeAsState("")

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    // MODAL 1 BOTON
    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }


    var showModal2Boton by remember { mutableStateOf(false) }


    // Definir el color del fondo al presionar
    val loginButtonColor = if (isPressed) {
        colorResource(id = R.color.colorRojo).copy(alpha = 0.8f) // más oscuro al presionar
    } else {
        colorResource(id = R.color.colorRojo)
    }
    // Animación de sombra
    val elevation by animateDpAsState(if (isPressed) 12.dp else 6.dp)

    // Animación y diseño
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.jsongps))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val texto8CaracteresTelefono = stringResource(R.string.telefono_son_8_digitos)


    // Lanzar la solicitud cuando se carga la pantalla
    LaunchedEffect(Unit) {
        scope.launch {
            idusuario = tokenManager.idUsuario.first()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .imePadding() // Acomoda el padding inferior cuando aparece el teclado
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()
        ) {


            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .height(225.dp)
                    .align(Alignment.CenterHorizontally) // Centrado horizontalmente
                    .padding(top = 24.dp) // Espaciado superior si quieres
            )


            // Título
            Text(
                text = stringResource(id = R.string.nueva_direccion),
                fontFamily = FontFamily(Font(R.font.arthura_medium)),
                color = Color.White,
                fontSize = 26.sp,
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )


            // Card de inicio de sesión
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(10.dp)
                ) {

                    BloqueEntradaGeneral(
                        text = nombre,
                        onTextChanged = { newText -> viewModel.setNombre(newText) },
                        maxLength = 100,
                        placeholderResId = R.string.nombre,
                        icon  = Icons.Filled.Person
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    BloqueEntradaGeneral(
                        text = telefono,
                        onTextChanged = { newText -> viewModel.setTelefono(newText) },
                        maxLength = 8,
                        placeholderResId = R.string.telefono,
                        icon  = Icons.Filled.Numbers,
                        keyboardType = KeyboardType.Phone
                    )

                    Spacer(modifier = Modifier.height(6.dp))


                    BloqueEntradaGeneral(
                        text = direccion,
                        onTextChanged = { newText -> viewModel.setDireccion(newText) },
                        maxLength = 400,
                        placeholderResId = R.string.direccion,
                        icon  = Icons.Filled.Map
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    BloqueEntradaGeneral(
                        text = puntoReferencia?: "",
                        onTextChanged = { newText -> viewModel.setPuntoReferencia(newText) },
                        maxLength = 400,
                        placeholderResId = R.string.punto_referencia,
                        icon  = Icons.Filled.House,
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = {
                            // Acción de login

                            keyboardController?.hide()

                            when {
                                nombre.isBlank() -> {
                                    modalMensajeString = ctx.getString(R.string.nombre_es_requerido)
                                    showModal1Boton = true
                                }

                                telefono.isBlank() -> {
                                    modalMensajeString = ctx.getString(R.string.telefono_es_requerido)
                                    showModal1Boton = true
                                }

                                telefono.length < 8 -> {
                                    modalMensajeString = texto8CaracteresTelefono
                                    showModal1Boton = true
                                }

                                direccion.isBlank() -> {
                                    modalMensajeString = ctx.getString(R.string.direccion_es_requerido)
                                    showModal1Boton = true
                                }

                                /// PUNTO REFERENCIA ES OPCIONAL

                                else -> {
                                    showModal2Boton = true
                                }
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, start = 24.dp, end = 24.dp)
                            .shadow(
                                elevation = elevation, // Cambia la sombra cuando se presiona
                                shape = RoundedCornerShape(25.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = loginButtonColor,  // Cambia color al presionar
                            contentColor = colorResource(R.color.colorBlanco),
                        ),
                        interactionSource = interactionSource // Para detectar la interacción
                    ) {
                        Text(
                            text = stringResource(id = R.string.guardar),
                            fontSize = 18.sp,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }


                if(showModal1Boton){
                    CustomModal1Boton(showModal1Boton, modalMensajeString, onDismiss = {showModal1Boton = false})
                }

                if (isLoading) {
                    LoadingModal(isLoading = isLoading)
                }

                resultado?.getContentIfNotHandled()?.let { result ->
                    when (result.success) {

                        1 -> {
                            // DIRECCION REGISTRADO
                            CustomToasty(
                                ctx,
                                stringResource(id = R.string.direccion_registrada),
                                ToastType.SUCCESS
                            )
                            scope.launch {
                                navController.navigate(Routes.VistaPrincipal.route) {
                                    popUpTo(0) { // Esto elimina todas las vistas de la pila de navegación
                                        inclusive =
                                            true // Asegura que ninguna pantalla anterior quede en la pila
                                    }
                                    launchSingleTop =
                                        true // Evita múltiples instancias de la misma vista
                                }
                            }
                        }
                        else -> {
                            // Error, mostrar Toast
                            CustomToasty(
                                ctx,
                                stringResource(id = R.string.error_reintentar_de_nuevo),
                                ToastType.ERROR
                            )
                        }
                    }
                }
            } // end-card


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f)) // Empuja la imagen hacia la derecha

                Image(
                    painter = painterResource(id = R.drawable.cubetapollo2),
                    contentDescription = stringResource(id = R.string.logotipo),
                    modifier = Modifier
                        .size(width = 120.dp, height = 100.dp)
                )
            }

            // CONFIRMAR PARA REGISTRAR NUEVA DIRECCION
            if(showModal2Boton){
                CustomModal2Botones(
                    showDialog = true,
                    message = stringResource(R.string.registrar_direccion),
                    onDismiss = { showModal2Boton = false },
                    onAccept = {
                        showModal2Boton = false

                        scope.launch {
                            viewModel.registrarNuevaDireccionRetrofit(
                                idusuario,
                                idzona.toString(),
                                latitud,
                                longitud,
                                latitudreal,
                                longitudreal
                            )
                        }
                    },
                    stringResource(R.string.si),
                    stringResource(R.string.no),
                )
            }

        }
    }
}



