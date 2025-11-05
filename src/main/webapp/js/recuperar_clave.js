/* 
 * Script: recuperar_clave.js
 * Autor: Marisela
 * Descripción: Validación del formulario de recuperación de contraseña en Marvela
 */

document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector("#formRecuperar");

  if (form) {
    form.addEventListener("submit", function (e) {
      const correo = form.querySelector("[name='correo']").value.trim();

      if (correo === "") {
        e.preventDefault();
        alert("Por favor ingresa tu correo electrónico.");
        return;
      }

      const regexCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!regexCorreo.test(correo)) {
        e.preventDefault();
        alert("El formato del correo no es válido.");
      }
    });
  }
});