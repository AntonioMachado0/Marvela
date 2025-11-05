<%-- 
    Document   : recuperar_contraseña
    Created on : 26 oct. 2025, 20:55:49
    Author     : Maris
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Recuperar contraseña - Ferretería Marvela</title>
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

            .btn-recuperar {
                font-size: 14px;
                padding: 8px 16px;
            }

            .input-group-text {
                background-color: #388e3c;
                color: #fff;
                border: none;
            }

            .text-volver {
                font-size: 13px;
                color: #388e3c;
            }

            .text-volver:hover {
                text-decoration: underline;
                color: #2e7d32;
            }
        </style>
    </head>
    <body>

        <!-- Íconos flotantes -->
        <i class="fas fa-wrench position-absolute" style="top: 10%; left: 5%; font-size: 80px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-hammer position-absolute" style="bottom: 10%; right: 5%; font-size: 80px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-tools position-absolute" style="top: 50%; left: 10%; font-size: 70px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-cogs position-absolute" style="bottom: 20%; right: 15%; font-size: 60px; color: rgba(0,0,0,0.05);"></i>

        <section class="gradient-form">
            <div class="container py-5 h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-xl-6">
                        <div class="card rounded-3 text-black shadow">
                            <div class="card-body p-md-5 mx-md-4">
                                <div class="text-center">
                                    <div class="logo-box">
                                        <img src="img/Login2.png" style="width: 80px;" alt="Logo Ferretería">
                                    </div>
                                    <h4 class="mt-1 mb-4">Recuperar contraseña</h4>
                                    <p class="mb-4">Ingresa tu correo para recuperar el acceso.</p>
                                </div>

                                <form method="POST" action="UsuarioController" id="formRecuperar">
                                    <input type="hidden" name="consultar_datos" value="recuperar">

                                    <div class="input-group mb-4">
                                        <span class="input-group-text bg-success text-white">
                                            <i class="fas fa-envelope"></i>
                                        </span>
                                        <input type="email" name="correo" class="form-control" placeholder="Correo electrónico" required>
                                    </div>

                                    <div class="text-center">
                                        <button class="btn btn-success btn-recuperar btn-block mb-2" type="submit">
                                            <i class="fas fa-paper-plane me-2"></i>Recuperar acceso
                                        </button>
                                    </div>

                                    <div class="text-center">
                                        <a href="acceso.jsp" class="text-volver">← Volver al login</a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger">
        <%= request.getAttribute("error") %>
    </div>
<% } %>
        <!-- Scripts -->
        <script src="js/recuperar_clave.js"></script>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"></script>
    </body>
</html>