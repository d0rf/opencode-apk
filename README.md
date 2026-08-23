# OpenCode APK

Client Android minimal (une WebView plein écran) pour le serveur OpenCode qui tourne sur l'Odroid.

## Télécharger

L'APK signé du dernier build de `master` est toujours à la même adresse :

**https://github.com/d0rf/opencode-apk/releases/latest/download/opencode.apk**

Chaque push sur `master` reconstruit l'APK et remplace cette release.

## Adresse du serveur

L'adresse par défaut est `http://odroid.tail9b91d6.ts.net:3002`. Elle n'est plus figée dans le
code : si la page ne charge pas, l'appli affiche un dialogue permettant de réessayer ou de saisir
une autre adresse, mémorisée ensuite dans les préférences.

Pour retrouver le port réel du serveur, sur l'Odroid :

```sh
ss -tlnp | grep -E ':(300[0-9]|4096)'
tailscale status   # vérifie aussi que le nom d'hôte n'a pas changé après réinstallation
```

## Signature

Par défaut le CI génère un keystore jetable à chaque build, donc la signature change et Android
refuse d'installer par-dessus : il faut désinstaller l'ancienne version.

Pour éviter ça, générer un keystore une fois et le stocker en secret de dépôt :

```sh
keytool -genkey -v -keystore opencode.keystore -alias opencode -keyalg RSA -keysize 2048 \
  -validity 10000 -storepass opencode -keypass opencode -dname "CN=OpenCode" -noprompt
base64 -w0 opencode.keystore
```

Coller la sortie dans *Settings → Secrets and variables → Actions → New repository secret*, sous le
nom `KEYSTORE_B64`. Les builds suivants réutiliseront cette clé et s'installeront en mise à jour.
Garder le fichier `opencode.keystore` hors du dépôt.

## Construire en local

```sh
./gradlew assembleRelease
```

Nécessite un SDK Android (plateforme 34) et un JDK 17.
