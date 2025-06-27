# 🎮 GameHub - Plataforma de Torneos de Videojuegos  

<div align="center">

![Java](https://img.shields.io/badge/Java-17-%23ED8B00?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1-%236DB33F?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-%23316192?logo=postgresql)
![JWT](https://img.shields.io/badge/JWT-Auth-%23000000?logo=jsonwebtokens)
![Swagger](https://img.shields.io/badge/Swagger-Docs-%2385EA2D?logo=swagger)
![Git](https://img.shields.io/badge/Git-ES-%23F05032?logo=git)

</div>

## 🌟 **Para Reclutadores Técnicos**  

**GameHub** es un backend robusto para plataformas de torneos de videojuegos, desarrollado con:  

- **Arquitectura RESTful** diseñada para alta escalabilidad  
- **Autenticación JWT** con roles diferenciados (ADMIN/PLAYER)  
- **Motor de emparejamiento** automático por rondas  
- **Documentación Swagger** completa para integración sencilla  
- **Pruebas unitarias** clave (JUnit 5 + Mockito)  

```java
// Ejemplo de endpoint seguro
@PostMapping("/tournaments")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Tournament> createTournament(@Valid @RequestBody TournamentDTO dto) {
    // Lógica de negocio
}
```

## 🚀 **Para Reclutadores No Técnicos**  

GameHub potencia la experiencia competitiva gamer con:  

✅ **Gestión inteligente de torneos** (creación, inscripción, resultados)  
✅ **Sistema de rankings** automático para motivar a los jugadores  
✅ **Chat integrado** para interacción entre competidores  
✅ **Panel de administración** para control total  

*"Imagine una plataforma donde los gamers compiten y conectan, con reglas claras y resultados en tiempo real. ¡Eso es GameHub!"*  

## 📊 **Métricas Clave**  

| Tecnología          | Uso                          | Beneficio                     |
|---------------------|------------------------------|-------------------------------|
| Spring Boot         | Backend principal            | Escalabilidad y rendimiento   |
| PostgreSQL          | Base de datos relacional     | Integridad de datos           |
| JWT                 | Autenticación segura         | Protección de cuentas         |

## 🔧 **Instalación Rápida**  

1. Clona el repositorio:  
```bash
git clone https://github.com/tu-usuario/gamehub-backend.git
```

2. Configura la base de datos (Docker):  
```bash
docker-compose up -d postgres
```

3. Ejecuta la aplicación:  
```bash
./mvnw spring-boot:run
```

Accede a la documentación: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  


<div align="center">
  
✨ *"Donde la competencia encuentra su plataforma"* ✨  

</div>

---

### 🔍 **Detalles Técnicos para CTOs**  

- **Patrón de Diseño**: MVC + Services  
- **Seguridad**: Spring Security + JWT  
- **CI/CD**: GitHub Actions (próximamente)  
- **API Docs**: OpenAPI 3.0  

```yaml
# Ejemplo de configuración JWT
gamehub:
  security:
    jwt-secret: ${JWT_SECRET}
    expiration-ms: 86400000 # 24h
```
