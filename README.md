<head># mBioLab - v01

## Projeto do software mBioLab - v01 - Biopdi
</head>

<body>
<br>
### Setup inicial

**********************************

<ol><li>Estrutura do projeto Maven JavaFX - Modelo MVC
<ol><li>java
<ol><li>br
<ol><li>model
<ol><li>bean
<ol><li>Essay
<li>Setup
<li>User</ol>
<li>dao
<ol><li>EssayDAO
<li>SetupDAO
<li>UserDAO</ol></ol>
<li>controller
<ol><li>Repository
<ol><li>ConnectionFactory</ol>
<li>SceneController
<ol><li>principalSceneController
<li>loginSceneController
<li>reportSceneController
<li>essaySceneController
<li>pastEssaySceneController
<li>configurationSceneController
<li>setupSceneController</ol></ol></ol>
<li>mBio_v1Main</ol></ol>
<ol><li>resources
<ol><li>View
<ol><li>principalScene.fxml
<li>loginScene.fxml
<li>reportScene.fxml
<li>essayScene.fxml
<li>pastEssayScene.fxml
<li>configurationScene.fxml
<li>setupScene.fxml</ol>
<li>resource
<ol><li>imagens e afins</ol></ol></ol><br>
<li>Injeção de dependências
<ol><li>Javafx-controls
<li>Javafx-fxml
<li>Spring Data jpa
<li>Spring security
<li>jSerialComm
<li>sqlite-jdbc(xerial)
<li>spring test
<li>spring security test
<li>junit-api
<li>junit-engine</ol><br>
<li>Inclusão de bibliotecas
<ol><li>jSerialComm
<li>JDK do JavaFX
<li>Sqlite-jdbc</ol><br>

<li>Criação do repositório, local e remoto, e push do projeto no GitHub
<ol><li>README
<li>gitignore
<li>Cada nova funcionalidade uma branch diferente. 
  <li>Ao finalizar e validar a função, merge com a branch main</ol><br>
<li>Criação das classes bean<br><br>

<li>Criação das classes DAO e ConnectionFactory, no pacote Repository<br><br>

<li>Criação do banco de dados SQLite, com endereço na raiz do projeto, aplicando sqlite-jdbc<br><br>

<li>Criação da classe SerialConnection, aplicando jSerialComm<br>

<ol><li>Ou incluir comandos no Controller do documento.fxml

  <li>Estudar qual a melhor alternativa</ol><br>

<li>Criação dos métodos de comunicação Serial<br>
<ol><li>Enviar dados 
<p>&#09private void outputInjection(String stg) {<br>
&#09PrintWriter output = new PrintWriter(port.getOutputStream(), true);<br>
&#09output.print(stg);<br>
&#09output.flush();<br>
&#09}<br></p>
<li>Receber dados 
<p>&#09private String inputValue() {<br>
Scanner s = new Scanner(port.getInputStream());<br>
&#09return s.nextLine();<br>
&#09}<br>
<li>Funções de monitoramento automático (Threads)
  <li>Gestão dos intervalos (Thread.sleep(2000))</ol><br>

<li>Implementação das Regras de Negócio<br><br>

<li>Interface Gráfica<br><br>

<li>Adicionais
<ol><li>Autoconexão
  <li>Construção da rotina do ensaio (flexibilidade na construção do método criado)</ol></ol>

</body>
********************************
  
<footer>
<br>### Comunicação Serial via biblioteca jSerialComm

<p>Repositório remoto: <a>https://github.com/Biopdi/mBiov01</a></p>


<div style="text-align:center">autor: Bruno Salata Lima<br>Git: /Brunosalata</div></footer>
