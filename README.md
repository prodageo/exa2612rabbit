# exa2612rabbit
A first rapid client for Distributed Computing

## Exemples d'appels simultanées sur la mémoire partagée
### Console 1
```
λ mvn -q exec:java -Dexec.args="-s 3"   
command : -s 3                          
14:35:51 : 3 -> 4                       
14:35:56 : 4 -> 5                       
14:36:1 : 5 -> 5                        
14:36:6 : 5 -> 7                        
Content of shared memory at : 14:36:11  
0 : 8                                   
```

### Console 2
```
λ  mvn -q exec:java -Dexec.args="-s 3"
command : -s 3
14:35:57 : 3 -> 4
14:36:2 : 4 -> 6
14:36:7 : 6 -> 8
14:36:12 : 8 -> 9
Content of shared memory at : 14:36:17
0 : 9
```
