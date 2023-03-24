
# General Notes

- ### Audio Scraping Service
  - will use __Youtubedl__ to pipe audio to users 
- ### User Management Service
  - User CRUD and Auth
- ### Audio Management Service
  - Audio CRUD
- ### Podcast Service
  - Generate Podcast
  - share podcast in Major Podcast Hosting Platforms (Spotify ...)
  - 
- ### Payment Service
    - Payment for Disk Space
    - maybe this service will have a table in the __Database Service__
- ### Database Service
  - Single Database containing all the tables (at least for now)
- ### Client Service
  - React app


## Open Questions (to be answered)

- What interactions will the user have with the audio files of other users (favorite, reactions? ...)


=========


To change your generateToken method to use RSA algorithm to sign the JWTs, you will need to make the following changes:
Replace the SignatureAlgorithm.HS256 parameter in the signWith method with SignatureAlgorithm.RS256.
Instead of using a secret key to sign the JWT, you will need to use an RSA private key. You can load the private key from a file or a keystore. For example:

  java

```java
private PrivateKey loadPrivateKey() throws Exception {
  // Load the private key from a file or a keystore
  // For example, you can load the private key from a PEM file:
  InputStream inputStream = new FileInputStream("privatekey.pem");
  byte[] privateKeyBytes = inputStream.readAllBytes();
  PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
  KeyFactory keyFactory = KeyFactory.getInstance("RSA");
  return keyFactory.generatePrivate(keySpec);
}
```
Note: You will need to handle the exceptions thrown by the code above.
Pass the loaded private key to the signWith method instead of the getSignInKey() method. For example:
less
.signWith(loadPrivateKey(), SignatureAlgorithm.RS256)

Here's the updated generateToken method using RSA algorithm:

typescript
```java
public String generateToken(
Map<String, Object> extraClaims,
UserDetails userDetails
) throws Exception {
return Jwts
.builder()
.setClaims(extraClaims)
.setSubject(userDetails.getUsername())
.setIssuedAt(new Date(System.currentTimeMillis()))
.setExpiration(new Date(System.currentTimeMillis() + 1000*60*10)) // 10 minutes
.signWith(loadPrivateKey(), SignatureAlgorithm.RS256)
.compact();

```
