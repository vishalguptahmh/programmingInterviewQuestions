

# HLD

## Basics

| Term          | Size                                      | Notes                                          |
| ------------- | ----------------------------------------- | ---------------------------------------------- |
| **Bit**       | 1 bit                                     | Smallest unit of data, either 0 or 1           |
| **Nibble**    | 4 bits                                    | Half a byte. One hex digit represents a nibble |
| **Byte**      | 8 bits = 2 nibbles                        | Can represent values from 0 to 255             |
| **Hex Digit** | 4 bits = 1 nibble                         | E.g., `F` = `1111`, `A` = `1010`               |
| **Word**      | 16 bits (2 bytes) (in some architectures) | Architecture-dependent                         |


![](images/HDL/biteTrick.png)



## Netowrk protocols
What is client server Model
What is Peer to Peer to model
Http ,tcp, udp, FTP, SMTP

The OSI (Open Systems Interconnection) Model is a set of rules that explains how different computer systems communicate over a network. OSI Model was developed by the International Organization for Standardization (ISO). The OSI Model consists of 7 layers and each layer has specific functions and responsibilities.

- Physical Layer
- Data Link Layer
- Network Layer
- Transport Layer
- Session Layer
- Presentation Layer
- Application Layer

we will focus on Application layer and transport layer

Application layer can be divided into two parts 
    - Client server protocol
        - !Http
        - FTP
        - SMTP
        - !WebSocket
    - Peer2Peer protocol
        - !webRTC


![](drawio/HLD/networklayer.drawio.png)

## Cap Theorem
- Latency vs throughput
- Availablity VS consisitency
- Performance vs Scalability
    - Vertical scaling
    - Horizontal scaling

![](images/HDL/cap1.png)

![](images/HDL/cap2.png)



## Monolithic vs Microservices
![](images/HDL/micro1.png)
![](images/HDL/micro2.png)
![](images/HDL/micro3.png)
![](images/HDL/micro4.png)
![](images/HDL/micro5.png)
![](images/HDL/micro6.png)
![](images/HDL/micro7.png)
![](images/HDL/micro8.png)
![](images/HDL/micro9.png)


## System Scaling to 1 Million

![](images/HDL/scalingSystem1.png)
![](images/HDL/scalingSystem2.png)
![](images/HDL/scalingSystem3.png)

## Consistent Hashing

![](images/HDL/consitanthasing1.png)
![](images/HDL/consistanthasing2.png)


## URL Shortner

![](images/HDL/urlShortner1.png)
![](images/HDL/urlShortner2.png)
![](images/HDL/urlShortner3.png)
![](images/HDL/urlShortner4.png)

## Back of Envelope Estimation


![](images/HDL/boee1.png)
![](images/HDL/boee2.png)
![](images/HDL/boee3.png)
![](images/HDL/boee4.png)
![](images/HDL/boee5.png)
![](images/HDL/boee6.png)
![](images/HDL/boee7.png)



# Reference : 

https://youtube.com/playlist?list=PL6W8uoQQ2c63W58rpNFDwdrBnq5G3EfT7&si=_3pZ4xCX_dCdcBBM

https://github.com/ashishps1/awesome-system-design-resources

https://leetcode.com/discuss/post/3611301/complete-system-design-case-studies-book-3ky6/

https://leetcode.com/discuss/post/2340482/system-design-template-that-landed-me-to-53gu/
https://leetcode.com/discuss/post/2341201/object-oriented-design-template-that-lan-co46/