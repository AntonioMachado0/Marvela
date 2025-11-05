<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Ferretería Marvela - Login</title>

        <!-- SweetAlert2 desde CDN -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <!-- Estilos externos -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap" rel="stylesheet"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.css" rel="stylesheet"/>

        <style>
            body {
                background: linear-gradient(135deg, #c8e6c9, #a5d6a7, #81c784);
                background-size: 600% 600%;
                animation: fondoMovimiento 20s ease infinite;
                font-family: 'Roboto', sans-serif;
            }

            @keyframes fondoMovimiento {
                0% {
                    background-position: 0% 50%;
                }
                100% {
                    background-position: 100% 50%;
                }
            }

            .gradient-form {
                height: 100vh;
            }
            .logo-box {
                background-color: #ffffff;
                border-radius: 50%;
                padding: 10px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                margin-bottom: 10px;
            }

            .btn-ingresar {
                font-size: 14px;
                padding: 8px 16px;
            }
            .input-group-text {
                background-color: #388e3c;
                color: #fff;
                border: none;
            }

            .text-recuperar {
                font-size: 13px;
                color: #388e3c;
            }

            .text-recuperar:hover {
                text-decoration: underline;
                color: #2e7d32;
            }
        </style>
    </head>
    <body>

        <!-- Íconos flotantes decorativos -->
        <i class="fas fa-wrench position-absolute" style="top: 10%; left: 5%; font-size: 80px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-hammer position-absolute" style="bottom: 10%; right: 5%; font-size: 80px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-screwdriver position-absolute" style="top: 50%; left: 10%; font-size: 60px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-tools position-absolute" style="bottom: 20%; right: 15%; font-size: 70px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-cogs position-absolute" style="top: 20%; right: 10%; font-size: 60px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-screwdriver-wrench position-absolute" style="top: 80%; left: 40%; font-size: 65px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-toolbox position-absolute" style="top: 30%; right: 30%; font-size: 70px; color: rgba(0,0,0,0.05);"></i>

        <section class="gradient-form">
            <div class="container py-5 h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-xl-8">
                        <div class="card rounded-3 text-black shadow">
                            <div class="row g-0">

                                <!-- Columna izquierda: formulario -->
                                <div class="col-lg-6">
                                    <div class="card-body p-md-5 mx-md-4">
                                        <div class="text-center">
                                            <div class="logo-box">
                                                <img src="img/Login2.png" style="width: 80px;" alt="Logo Ferretería">
                                            </div>
                                            <h4 class="mt-1 mb-4">Ferretería Marvela</h4>
                                            <p class="mb-4">Accede a tu cuenta.</p>
                                        </div>

                                        <form id="ingresar" action="consultar_datos" method="post">
                                            <input type="hidden" name="accion" value="ingresar">

                                            <!-- Campo de correo -->
                                            <div class="input-group mb-4">
                                                <span class="input-group-text bg-success text-white">
                                                    <i class="fas fa-envelope"></i>
                                                </span>
                                                <input type="text" name="correo" id="correo" class="form-control" placeholder="Correo electrónico" required>
                                            </div>

                                            <!-- Campo de contraseña -->
                                            <div class="input-group mb-4">
                                                <span class="input-group-text bg-success text-white">
                                                    <i class="fas fa-lock"></i>
                                                </span>
                                                <input type="password" name="clave" id="clave" class="form-control" placeholder="Contraseña" required>
                                            </div>

                                            <!-- Botón de ingreso -->
                                            <div class="text-center">
                                                <button class="btn btn-success btn-ingresar btn-block mb-2" type="submit">
                                                    <i class="fas fa-sign-in-alt me-2"></i>INGRESAR
                                                </button>
                                            </div>

                                            <!-- Enlace recuperar contraseña -->
                                            <div class="text-center">
                                                <a href="recuperar_contraseña.jsp" class="text-recuperar">¿Olvidaste tu contraseña?</a>
                                            </div>

                                            <div class="text-center mt-2">
                                                <a href="registrar_empleado.jsp" class="text-success fw-semibold text-decoration-none">
                                                    Regístrar Administrador
                                                </a>
                                            </div>
                                        </form>
                                    </div>
                                </div>

                                <!-- Columna derecha: imagen -->
                                <div class="col-lg-6 d-flex align-items-center justify-content-center bg-white">
                                    <img src="img/login.png" alt="Bienvenida Ferretería Marvela" class="img-fluid p-4">
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <script>
            window.addEventListener("pageshow", function (event) {
                if (event.persisted) {
                    window.location.reload();
                }
            });
        </script>
        <!-- Scripts en orden correcto -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"></script>
        <script src="js/acceso.js"></script>
        <script src="js/salir.js"></script>
    </body>
</html>