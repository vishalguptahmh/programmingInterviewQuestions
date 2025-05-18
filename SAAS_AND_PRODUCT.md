

### SAAS - software as a service
It is a cloud-based software delivery model where customers pay for software products on a subscription basis.


### [IAM](https://auth0.com/docs/get-started/identity-fundamentals/identity-and-access-management) - Identity Access Management

Identity and access management provides control over user validation and resource access. Commonly known as IAM, this technology ensures that the right people access the right digital resources at the right time and for the right reasons.

In IAM, a user account is a digital identity. User accounts can also represent non-humans, such as software, Internet of Things devices, or robotics.

IAM is a discipline and a type of framework for solving the challenge of secure access to digital resources.

Authentication is the verification of a digital identity. Someone (or something) authenticates to prove that they’re the user they claim to be. 

Authorization is the process of determining what resources a user can access. 

authentication proves a user’s identity, while authorization grants or denies the user’s access to certain resources. 

### [Identity Providers](https://auth0.com/docs/get-started/identity-fundamentals/identity-and-access-management#identity-providers)
Its very difficult to create account,login to multiple applications and filling forms.
its very hard to remember all password also

here comes IP which will create your information and give rights to other application that you are valid idenity.

Identity providers don’t share your authentication credentials with the apps that rely on them. Slate, for example, doesn’t ever see your Google password. Google only lets Slate know that you’ve proven your identity. 

 Other identity providers include social media (such as Facebook or LinkedIn), enterprise (such as Microsoft Active Directory), and legal identity providers (such as Swedish BankID).


### Authentication Factors
Authentication factors are methods for proving a user’s identity. IAM systems require one or many authentication factors to verify identity.They commonly fall into these basic types:

| Factor type | Examples |
| -------- | -------- |
| Knowledge (something you know)   | Pin, password  |
| Possession (something you have)  | Mobile phone, encryption key device   |
| Inherence (something you are)    | Fingerprint, facial recognition, iris scan  |


Authentication and authorization standards are open specifications and protocols

Below IAM industry standards are considered the most secure, reliable, and practical to implement:

- OAuth 2.0
- OpenID connect (OIDC)
- json web tokens (JWTs)
- SAML (Security Assertion Markup langaguge)
- WS-FED


### [Auth0](https://auth0.com/docs/get-started/identity-fundamentals/introduction-to-auth0)

### OAuth 2.0
OAuth 2.0 is an authorization framework that allows applications to access resources on behalf of a user without sharing the user's credentials. It's widely used for secure data sharing between different applications.

#### Roles in OAuth 2.0
- Resource owner: The user who owns the data (e.g., photos on Google Drive).
- Client : The application requesting access to the user's data (e.g., a printing service).
- Authorization Server : Issues access tokens after user consent (e.g., Google).
- Resource Server : Hosts the protected resources (e.g., Google Drive).

#### Example
Let's say you want to print photos from your Google Drive using a third-party printing service called Print Express.

Step 1 : Authorization Request
 - Print Express : (Client) requests authorization from you (Resource Owner) to access your photos on Google Drive.
 - You are redirected to Google's authorization page.

Step 2  : User Consent
- You grant Print Express permission to access your photos.
- Google (Authorization Server) redirects you back to Print Express with an authorization code.

Step 3 : Token Exchange
- Print Express exchanges the authorization code for an access token with Google.
- This access token is used to access your photos on Google Drive.

Step 4 : Resource Access
- Print express uses the access token to retrieve your photos from Google Drive (Resource Server).
- You don't need to share your Google credentials with Print Express.

![oauth 2.0 flow diagram](drawio/oauth2.drawio.png)


#### Key benefits

- Security : No sharing of credentials.
- Control : Users can revoke access at any time.
- Flexiblity : Supports various client types (web, mobile, desktop).

#### Common grant types
- Authorization Code Grant: Used for web apps.
- Imlicit Grnant : Used for clients that cannot store or handle client secrets securely (e.g., JavaScript apps).
- Client Credentials Grant: Used for server-to-server interactions.






## SSO
Single Sign ON 

It is an authentication method where user can login to multiple application from multiple devices from single set of credentials.
It works best on trust relationship setupup between applications (Service providers) and identity providers.

One-click secure access to all resources your employees need. This includes access to resources both on-prem or in the cloud. Let your team focus on work, rather than trying to remember passwords.

Login to portal > Single Sign on creds > get acess to all app ex (social,google directory service, Microsoft AD, LDAP)

### SAML
![](
https://mermaid.ink/img/pako:eNp1U01v2zAM_SsCTxvgFvFHmtiHAfmOixUz5u5SawfVZhNjsZRJcrosyH-fLNtphnU6PZKP5CMlnSAXBUIEG8n2W_I4pZxyVT-35jeFknJiziRrMGF5jkqhImnyvQ1Ms6kUr01MYlFKzLUiWpC46AnrCyH5kj6azMnDZ_IV1V5whQ3VlkJe_NU5RXkoc0ykOJQFyoxC5yG9i3xIk48Uui6zLE3IBjlKpvHS5GeNSneMuGEc2K4srhmtjI5y3w5ZKmJEcI1FN-87-uICuS718Upg77pSaLbwJnGeGdNsKcfy8L7ERdt_UuttUyq3QrvY0mbnEv-rftXV72-hbmo9s_xHu2TyWurtP4ndXBNyc_OJzCifWWBewdSCOeVzCxaULyxYUr60YEX5yoI15WsLYspjC-4pBwcqlBUrC_O2To1ACmaqCilEBhb4wuqdpkD52VBZrUV65DlEL2yn0AEp6s0WIi1rY9T75srmJTOrry6UPeNPQlQ9aSObVn26GQvlTNRcQ-QHvmVDdIJfEHn-4HYUhneeOxx7A9_1hg4cIXK94HZwNx4PwuEg9EYjPzg78Ns2sHw_dF038Jtw4DlgtqyFfGj_jv1C5z-UmQ8J)


### Different types of Single Sign On Mechanisms

| Mechanism        | Protocol          | Common Use Case           | Format | Cloud-Friendly |
| ---------------- | ----------------- | ------------------------- | ------ | -------------- |
| SAML             | SAML 2.0          | Enterprise Web Apps       | XML    | ✅              |
| OAuth 2.0 + OIDC | JSON + HTTP       | Web, Mobile, APIs         | JSON   | ✅✅             |
| Kerberos         | Kerberos          | On-Prem AD Networks       | Binary | ❌              |
| LDAP             | LDAP              | Legacy Systems            | Text   | ❌              |
| CAS              | Custom Protocol   | Academic Environments     | Text   | ☑️ (limited)   |
| Smart Card       | Certificate-Based | High-Security Enterprises | X.509  | ❌              |
| Social Login     | OAuth + OIDC      | Consumer Web/Mobile       | JSON   | ✅✅             |

### SAML vs Auth2.0

Common Steps:

    User tries to access a protected app.

    App redirects browser to Identity Provider (SSO).

    User logs in at IdP.

    IdP sends a response (SAML Assertion or ID Token) back to the app via the browser.

    App verifies the response and creates a session.




| Aspect               | **SAML 2.0**                      | **OAuth 2.0 + OIDC**                         |
| -------------------- | --------------------------------- | -------------------------------------------- |
| **Purpose**          | Authentication only               | OAuth: Authorization; OIDC: Authentication   |
| **Format**           | XML                               | JSON / JWT                                   |
| **Transport**        | HTTP Redirect / POST (Base64 XML) | HTTPS (JSON, tokens in URL or body)          |
| **Token Type**       | SAML Assertion (XML)              | ID Token (JWT), Access Token (JWT or opaque) |
| **Audience**         | Enterprises, legacy web apps      | Modern web, mobile, APIs                     |
| **SP Role**          | Service Provider                  | Relying Party / Client                       |
| **IdP Role**         | Identity Provider                 | Authorization Server + IdP (in OIDC)         |
| **Extensibility**    | Limited, XML schema bound         | High (JSON-based, supports scopes/claims)    |
| **Common Providers** | Okta, ADFS, Ping, Azure AD        | Google, GitHub, Auth0, Firebase, AWS Cognito |

SAML is identity-assertion centric:
“Here’s a digitally signed XML document stating who this user is.”

OIDC (on top of OAuth) is token-centric:
“Here’s a signed JWT ID token that asserts the user’s identity, plus access token for APIs if needed.”

Not all Single sign follow above 5 steps
ex  : 
- Kerberos(Uses tickets, Authentication is handled at OS level not app level)
- LDAP Authentication : 
    - App connects directly to an LDAP server (like Active Directory) to authenticate.
    - SSO works when combined with Kerberos or a reverse proxy.
- Smart Card / Certificate-Based
    - Authentication happens using client-side certificates or physical smart cards.
    - Often plugged into the browser or OS login.


Trust Model of SAML

![](https://mermaid.ink/img/pako:eNp1UstqwzAQ_JXFl6aQ_EAOhThPJ3ZiogQKdQ-qvSSiqexackIo_feuJdl5tLVhQGJmdnZXX16aZ-j1vV3Jiz1s_ETaX1Vv9irx2CAKYVDpPUwO-Qk6LO4FUmjBNWaPiZdIoG_8QsQY0hLpWhm6XONnhUpD5zkKifhqmRPLVGIn73gnQTWKUhzJAt7x3EqmJPHL_KSwhBIzUWKqFegcgiy2qmufVjYjWc0gOorjXaqWFTjWkR9EZsK7dFxXJUKlhNyB0jkVhqJ6O4j0Jtuc5Ns6GCdzlFqkxoPrOlzLWrgizXxqf_Kzo1Uk1yKXLTu86jdesY26I9ats4t5ZCd6aeBCtOmp8oP6K_zydmsKlapVXGZA25c0ZJ6mdGkEbtHQ6z3BxODU4MxgYHBucGEwNBgZXCYSZXb3sCLUnAJzYKirAuI9V9i8p4F7JaRS8NEQ7cr7MKY56zMEoy4Mhgy267ALsW1vcdWe76Zutmf22ji1lGFD-VWIUR3GVv-aj1xC61173Ji7Nkz3vqtlDiM3Cu_7B1HdHR0)




#### How IdP ensures it’s valid:
1. Signature Validation (Optional but Recommended):
    - The SP signs the AuthnRequest using its private key.
    - The IdP validates the signature using the SP’s public certificate (previously exchanged during metadata setup).
    - This confirms:
        - The request came from a trusted SP.
        - The request was not tampered with.

1. SP Metadata:
    - SP and IdP trust each other based on pre-shared metadata.
    - Metadata contains:
        - SP entity ID
        - SP public key
        - Assertion Consumer Service (ACS) URLs
        - Binding method (POST, Redirect)

    - IdP uses this metadata to:
        - Check if the incoming request is from a known SP.
        - Verify request bindings and endpoints match expectations.

1. Timestamp & Replay Protection:
    - The AuthnRequest contains timestamps (IssueInstant).
    - IdP can reject requests that are too old or reused.


### Reference 
Auth2.0 :
https://www.youtube.com/watch?v=CPbvxxslDTU&ab_channel=InterSystemsLearningServices

IDP : https://www.youtube.com/watch?v=hDRVq7T2A3M&ab_channel=JumpCloud

PKI : https://www.youtube.com/watch?v=5OqgYSXWYQM&ab_channel=PaulTurner

Single Sign on : https://www.youtube.com/watch?v=-hWaEy7_XQU&ab_channel=AutomatedTesting


AWS LAMBDA : https://youtu.be/qYM5UwhIkPI?si=OfWXhqUStBDQBZWd

OIDC : https://openidconnect.net/


#### Other Things : 

- TCP RELAY