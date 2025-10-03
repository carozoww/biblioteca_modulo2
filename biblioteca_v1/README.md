# biblioteca
Primera repo de Avanzada.

#Creacion del proyecto local y uso.

1) Primero Parense en donde tengan ubicados sus repos de Visual Studio.
  ![image](https://user-images.githubusercontent.com/73909317/162352678-d1b5d29e-24d9-4241-a07c-3219e2817cbb.png)
  
2) Abran un git bash dentro de ese directorio
  ![image](https://user-images.githubusercontent.com/73909317/162352796-b24c4d92-f297-48c4-85c7-fb4a390038dd.png)
  
3) Clonen este repositorio de la siguiente manera:
  git clone https://github.com/carozoww/biblioteca.git
  
4) Una vez creada la carpeta pueden abrirla como proyecto dentro del Visual studio.

#Pasos para subir cambios a la repo de manera sencilla:
1) Parense adentro del directorio 'Biblioteca' que se trajeron de github y abran un git bash.
2) Creanse una rama local de la siguiente manera para trabajar sus cambios: git checkout -b <nombre_de_tu_rama_local>
  ![image](https://user-images.githubusercontent.com/73909317/162354950-34f5d6ac-ed00-4b15-89b9-ef3eeef19b14.png)

4) Una vez que esten bien con los cambios, pueden verlos en el solution explorer a la deracha en la pestania de git changes 
->![image](https://user-images.githubusercontent.com/73909317/162354398-64525ecb-d725-4a5e-b870-55bf4a7fbf25.png)
4)Cuando esten seguros de sus cambios, simplemente pueden hacer click derecho + Stage y se cambiara de estado los cambios que proponen y un mensaje explicando el mismo
![image](https://user-images.githubusercontent.com/73909317/162354479-2303512f-eb5a-4b75-8f7e-948a25e64424.png)

![image](https://user-images.githubusercontent.com/73909317/162354564-3f445a27-fb5e-49cc-9a88-4df560216e28.png)
5) Una vez que esten seguros de lo que quieran subir, le dan a Commit Staged, y les dara un prompt de que el commit
  fue creado de manera local.
![image](https://user-images.githubusercontent.com/73909317/162354639-8cc8c7f1-574d-45a1-80a3-ecc085124e72.png)
6) Ahora ya estan prontos para hacer un push. En el git bash que deberian tener abierto del paso #1 escriben 
  git push -u origin <nombre_de_tu_rama_local>
  ![image](https://user-images.githubusercontent.com/73909317/162355039-a536bc9b-f8e7-4d9e-826d-fb35eb2e3df2.png)

7) Vuelvan a git para ver si sus cambios fueron creados con un pull request.
  ![image](https://user-images.githubusercontent.com/73909317/162355076-193ee7bf-ab2c-49d3-8b33-b015db38d609.png)

8) Revisan si los cambios subidos estan correctos y que no generen conflictos. Si lo hacen arreglenlos.
![image](https://user-images.githubusercontent.com/73909317/162355175-63def3d0-85c4-4ab4-81d3-6b4660b1b34b.png)
Asegurense de que la rama que mandaron al repositorio sea la misma del local.
9) Una vez mergeado le pueden dar mergear y borrar la rama.
![image](https://user-images.githubusercontent.com/73909317/162355257-37a9c9c4-ab75-4765-860f-e78e8405ae0f.png)
![image](https://user-images.githubusercontent.com/73909317/162355282-364c18c5-a59d-4a15-b3ae-b7b8dd7d4a75.png)
10) Pueden volver al git bash y hacer un git checkout master. Esto hara que vuelvan a la rama principal. Una vez ahi
    pueden borrar su rama local tirando: git branch -D <nombre_de_tu_rama_local>
11) Luego hacen un pull para traerse los cambios mergeados
![image](https://user-images.githubusercontent.com/73909317/162355475-67cc87a5-2097-4ada-b94d-2b49845d743c.png)
12) Pueden Volver al paso 1 para seguir el flujo de subir y hacer cambios.
