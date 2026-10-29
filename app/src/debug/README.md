# Conexión HTTPS durante las pruebas locales

Norton Web/Mail Shield inspecciona HTTPS en este equipo con una autoridad certificadora local. Windows confía en ella, pero el emulador no la incluye de forma predeterminada.

Esta carpeta configura únicamente la variante debug para aceptar su certificado público al consultar el dominio exacto de nuestra API en MockAPI. Se mantienen las autoridades del sistema, la comprobación del nombre del servidor y HTTPS. No se aceptan certificados arbitrarios ni se habilita HTTP.

El certificado y la configuración no se incluyen en release. Si Norton regenera su autoridad local, habrá que actualizar el certificado público de esta carpeta. No se almacena ninguna clave privada.
