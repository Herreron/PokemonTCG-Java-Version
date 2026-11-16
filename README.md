# Pokémon TCG Pocket — Versión Java

Simulación por consola del juego de cartas **Pokémon TCG Pocket** (expansión *Genetic Apex*) para **dos jugadores** que comparten el mismo computador y se turnan el teclado. Cada jugador arma su campo, juega cartas, ataca con sus Pokémon y trata de ser el primero en conseguir **3 puntos de premio**.

Mini Proyecto 1 · Universidad del Valle · Java 21

## Integrantes

- Juan Herrera
- Miguel Marín
- Juan Camilo Bolaños Riascos

## Requisitos

- **Java 21** (JDK). Para comprobarlo, abre una terminal y escribe `java -version`.
- Una terminal. El juego no usa librerías externas.

## Cómo ejecutarlo

1. Descarga o clona el repositorio y abre una terminal **en la carpeta del proyecto** (la que contiene la carpeta `App`).
2. Compila el juego:

   ```
   javac -d out -sourcepath App/src App/src/App.java
   ```

3. Ejecútalo:

   ```
   java -cp out App
   ```

Estos mismos pasos funcionan en Windows, Linux y macOS. Si usas **Visual Studio Code** con la extensión de Java, también puedes abrir la carpeta del proyecto y ejecutar el archivo `App.java`.

> **Consejo para Windows:** si las tildes se ven raras en la consola, escribe `chcp 65001` antes de ejecutar el juego.

Para salir en cualquier momento pulsa `Ctrl + C`, o elige la opción `0` en el menú principal.

## Cómo se juega

### Objetivo

Ganas el duelo en cuanto ocurre **una** de estas tres cosas:

1. Consigues **3 puntos de premio**.
2. Tu rival **no puede robar carta** al inicio de su turno porque su mazo se quedó vacío.
3. Tu rival **se queda sin Pokémon en juego** (ni activo ni en la banca).

Al final el juego anuncia al ganador con un mensaje de felicitación.

### Antes de empezar

1. En el menú principal elige `1` para iniciar una partida rápida.
2. Cada jugador escribe su **nombre** (no pueden ser iguales).
3. Cada jugador elige su **mazo temático**: fuego, agua, eléctrico o psíquico. Cada mazo tiene 20 cartas y se baraja solo.
4. Se reparten **5 cartas** a cada jugador. Si en esa mano no hay ningún Pokémon básico, la mano vuelve al mazo, se baraja y se reparte otra vez (mulligan automático).
5. Cada jugador, por turnos, elige su **Pokémon activo** y, si quiere, hasta **2 Pokémon básicos para la banca**. Cuando termina el jugador 1, la pantalla se limpia y se le pasa el teclado al jugador 2.
6. Una moneda decide quién juega primero.

### Tu turno

Al empezar tu turno, el juego hace dos cosas por ti:

- **Robas 1 carta** de tu mazo.
- **Recibes 1 energía** para adjuntar. Si no la usas en ese turno, se pierde (no se acumula).

Después ves el estado del campo y el menú de acciones. Puedes elegir las acciones en el orden que quieras, y cada una tiene sus límites:

```
TURNO 1 — Gary

--- RIVAL (Ash) ---
Puntos de: Ash: 0/3
[Activo] Growlithe (Fase 1, 70/70 PS) | Energia adjunta: 0

--- TU CAMPO (Gary) ---
Puntos de: Gary: 0/3
[Activo] Psyduck (Fase 1, 60/60 PS) | Energia adjunta: 0
Energia disponible: 1

--- Mano de Gary (5 cartas) ---
1. Poke Ball
2. Golduck
3. Magikarp
4. Blastoise
5. Poke Ball

-----------------------------------
Acciones disponibles:
1. Adjuntar energia de zona
2. Jugar carta de entrenador u objeto
3. Colocar Pokemon basico en la banca
4. Retirar Pokemon activo
5. Atacar
6. Evolucionar Pokemon
7. Pasar turno
-----------------------------------
Elige una accion:
```

| Opción | Qué hace |
|---|---|
| **1. Adjuntar energía** | Pones la energía del turno en uno de tus Pokémon en juego (el activo o uno de la banca). Solo puedes adjuntar **1 por turno**. |
| **2. Jugar carta de entrenador u objeto** | Juegas una carta de tu mano. Los **objetos** no tienen límite por turno; de las cartas de **soporte** solo puedes jugar **1 por turno**. |
| **3. Colocar Pokémon en la banca** | Pasas un Pokémon básico de tu mano a la banca. La banca admite **máximo 2**. |
| **4. Retirar Pokémon activo** | Cambias tu Pokémon activo por uno de la banca. Debes pagar su **costo de retirada**: se descartan esa cantidad de energías del Pokémon que se retira. |
| **5. Atacar** | Eliges uno de los ataques de tu Pokémon activo. Necesitas tener adjuntas **al menos tantas energías como cuesta el ataque** (las energías no se gastan al atacar). Solo puedes atacar **1 vez por turno**, y **el jugador que empieza la partida no puede atacar en su primer turno**. |
| **6. Evolucionar Pokémon** | Si tienes en la mano la evolución de uno de tus Pokémon en juego (por ejemplo Charmeleon para un Charmander), la juegas sobre él. Conserva la energía adjunta y el daño recibido. Un Pokémon **no puede evolucionar el mismo turno en que se coloca en juego**, ni puede evolucionar dos veces en un turno, y nadie evoluciona en su primer turno. |
| **7. Pasar turno** | Termina tu turno. La pantalla se limpia y el teclado pasa al otro jugador. |

Después de cada acción el juego te avisa qué pasó (o qué te falta para poder hacerla) y espera a que pulses **ENTER** para continuar.

### Combate

- El daño del ataque se aplica al **Pokémon activo del rival**.
- **Debilidad:** si el tipo del *ataque* es al que el Pokémon defensor es débil, el ataque hace **+20 de daño**. En esta versión, los Pokémon de **Fuego** son débiles a los ataques de **Agua**, y los Pokémon de **Agua** son débiles a los ataques **Eléctricos**. (Cuenta el tipo del ataque, no el del Pokémon que ataca: por ejemplo, Picotazo de Zapdos ex es un ataque Incoloro.)
- Cuando un Pokémon se queda **sin PS**, sale del campo y quien lo derrotó gana **1 punto de premio** (o **2 puntos** si era un Pokémon **ex**).
- El dueño del Pokémon derrotado **elige a otro Pokémon de su banca** como nuevo activo. Si no le queda ninguno, pierde el duelo.

### Cartas de entrenador

| Carta | Tipo | Efecto |
|---|---|---|
| Investigacion del Profesor | Soporte | Descartas toda tu mano y robas 2 cartas del mazo. |
| Misty | Soporte | Adjuntas 3 energías a uno de tus Pokémon de tipo Agua que esté en juego. |
| Pocion | Objeto | Curas 20 PS a uno de tus Pokémon heridos en juego. |
| Poke Ball | Objeto | Buscas en tu mazo 1 Pokémon básico y lo pones en tu mano. |
| Turbo Velocidad | Objeto | Durante este turno, el costo de retirada de tu Pokémon activo baja en 1. |

### Mazos

Cada mazo tiene exactamente **20 cartas**: 2 copias de cada una de las 10 cartas distintas que lo forman, y siempre incluye al menos 6 Pokémon básicos.

| Mazo | Pokémon | Entrenador |
|---|---|---|
| 🔥 Fuego | Charmander, Charmeleon, Charizard, Moltres ex, Growlithe, Arcanine | Pocion, Poke Ball, Turbo Velocidad, Investigacion del Profesor |
| 💧 Agua | Squirtle, Wartortle, Blastoise, Magikarp, Gyarados, Articuno ex, Psyduck, Golduck | Poke Ball, Misty |
| ⚡ Eléctrico | Pikachu, Raichu, Zapdos ex, Electabuzz, Jigglypuff, Wigglytuff, Snorlax | Pocion, Poke Ball, Investigacion del Profesor |
| 🔮 Psíquico | Mewtwo ex, Gastly, Haunter, Gengar, Jigglypuff, Wigglytuff, Snorlax | Pocion, Turbo Velocidad, Investigacion del Profesor |

### Pokémon disponibles

La columna **Etapa** es la que muestra el juego: **Fase 1** es un Pokémon básico (el único que puedes colocar al empezar o en la banca); las Fases 2 y 3 son evoluciones. Los Pokémon **ex** valen 2 puntos de premio al ser derrotados. Los nombres de cartas y ataques aparecen en el juego sin tildes.

| Pokémon | Tipo | Etapa | PS | Retirada | Ataques: nombre (energías) → daño | Evoluciona de |
|---|---|---|---|---|---|---|
| Charmander | 🔥 Fuego | Fase 1 | 60 | 1 | Ascuas (1) → 30 | — |
| Charmeleon | 🔥 Fuego | Fase 2 | 90 | 2 | Garras de Fuego (3) → 60 | Charmander |
| Charizard | 🔥 Fuego | Fase 3 | 150 | 2 | Giro Fuego (4) → 150 | Charmeleon |
| Moltres ex | 🔥 Fuego | Fase 1 | 130 | 2 | Aleteo (1) → 30<br>Danza Infernal (3) → 70 | — |
| Growlithe | 🔥 Fuego | Fase 1 | 70 | 1 | Mordisco (1) → 10 | — |
| Arcanine | 🔥 Fuego | Fase 2 | 130 | 2 | Lanzallamas (3) → 90 | Growlithe |
| Squirtle | 💧 Agua | Fase 1 | 60 | 1 | Placaje (1) → 20 | — |
| Wartortle | 💧 Agua | Fase 2 | 80 | 1 | Salpicadura (2) → 40 | Squirtle |
| Blastoise | 💧 Agua | Fase 3 | 150 | 3 | Hidrobomba (3) → 80 | Wartortle |
| Magikarp | 💧 Agua | Fase 1 | 30 | 1 | Chapoteo (1) → 10 | — |
| Gyarados | 💧 Agua | Fase 2 | 150 | 4 | Hiperrayo (4) → 100 | Magikarp |
| Articuno ex | 💧 Agua | Fase 1 | 130 | 2 | Viento Helado (1) → 30<br>Ventisca (3) → 100 | — |
| Psyduck | 💧 Agua | Fase 1 | 60 | 1 | Dolor de Cabeza (1) → 20 | — |
| Golduck | 💧 Agua | Fase 2 | 90 | 1 | Rayo Aurora (2) → 60 | Psyduck |
| Pikachu | ⚡ Eléctrico | Fase 1 | 60 | 1 | Mordisco (1) → 20 | — |
| Raichu | ⚡ Eléctrico | Fase 2 | 100 | 1 | Rayo (3) → 140 | Pikachu |
| Zapdos ex | ⚡ Eléctrico | Fase 1 | 130 | 2 | Picotazo (1) → 30<br>Tormenta Electrica (3) → 100 | — |
| Electabuzz | ⚡ Eléctrico | Fase 1 | 80 | 1 | Puño Trueno (2) → 40 | — |
| Mewtwo ex | 🔮 Psíquico | Fase 1 | 150 | 2 | Ataque Psiquico (2) → 50<br>Psicorrayo (3) → 150 | — |
| Gastly | 🔮 Psíquico | Fase 1 | 50 | 1 | Lengüetazo (1) → 10 | — |
| Haunter | 🔮 Psíquico | Fase 2 | 70 | 1 | Tinieblas (2) → 40 | Gastly |
| Gengar | 🔮 Psíquico | Fase 3 | 130 | 2 | Bola Sombra (3) → 80 | Haunter |
| Jigglypuff | ⭐ Incoloro | Fase 1 | 70 | 1 | Golpe (1) → 20 | — |
| Wigglytuff | ⭐ Incoloro | Fase 2 | 100 | 1 | Vozarron (2) → 60 | Jigglypuff |
| Snorlax | ⭐ Incoloro | Fase 1 | 150 | 3 | Golpe Cuerpo (3) → 100 | — |

### Consejos para tu primera partida

- Adjunta tu energía **todos los turnos**, aunque no vayas a atacar todavía: si no la usas, se pierde.
- Mira el costo de cada ataque antes de decidir en qué Pokémon adjuntas la energía.
- Procura tener siempre al menos **un Pokémon en la banca**: si tu activo es derrotado y no te queda ninguno, pierdes el duelo.
- Cuando el juego diga "Entrega la consola", el otro jugador no debe mirar la pantalla: allí aparece la mano del jugador en turno.
