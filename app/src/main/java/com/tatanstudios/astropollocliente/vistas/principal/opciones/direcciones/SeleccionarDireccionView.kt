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
import com.tatanstudios.astropollocliente.viewmodel.BorrarDireccionViewModel
import com.tatanstudios.astropollocliente.viewmodel.SeleccionarDireccionViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SeleccionarDireccionScreen(navController: NavHostController,
                                  id: Int, // id direccion
                                  nombre: String,
                                  telefono: String,
                                  direccion: String,
                                  puntoReferencia: String?,
                                  viewModelSeleccionar: SeleccionarDireccionViewModel = viewModel(),
                                  viewModelBorrar: BorrarDireccionViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val resultadoSeleccionar by viewModelSeleccionar.resultado.observeAsState()
    val isLoadingSeleccionar by viewModelSeleccionar.isLoading.observeAsState(false)

    val resultadoBorrar by viewModelBorrar.resultado.observeAsState()
    val isLoadingBorrar by viewModelBorrar.isLoading.observeAsState(false)

    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }


    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(if (isPressed) 12.dp else 6.dp)

    val keyboardController = LocalSoftwareKeyboardController.current


    var showModal2Boton by remember { mutableStateOf(false) }



    // Definir el color del fondo al presionar
    val loginButtonColor = if (isPressed) {
        colorResource(id = R.color.colorRojo).copy(alpha = 0.8f) // más oscuro al presionar
    } else {
        colorResource(id = R.color.colorRojo)
    }


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



                    Button(
                        onClick = {
                            // Acción de login

                            keyboardController?.hide()



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


                if (isLoadingSeleccionar) {
                    LoadingModal(isLoading = isLoadingSeleccionar)
                }

                if (isLoadingBorrar) {
                    LoadingModal(isLoading = isLoadingBorrar)
                }

                resultadoSeleccionar?.getContentIfNotHandled()?.let { result ->
                    when (result.success) {

                        1 -> {


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


                resultadoBorrar?.getContentIfNotHandled()?.let { result ->
                    when (result.success) {

                        1 -> {


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

                        /*scope.launch {
                            viewModel.registrarNuevaDireccionRetrofit(
                                idusuario,
                                idzona.toString(),
                                latitud,
                                longitud,
                                latitudreal,
                                longitudreal
                            )
                        }*/
                    },
                    stringResource(R.string.si),
                    stringResource(R.string.no),
                )
            }

        }
    }
}
