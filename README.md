<head># mBioLab - v01

# Projeto do software mBioLab - v02 - Biopdi
</head>
Projeto desenvolvido para a criação do software mBioLab<span style="font-size:10px; vertical-align: super"> 
&copy;</span> para o gerenciamento e comunicação serial com os equipamentos Biopdi<span 
style="font-size:10px; vertical-align: super"> ®</span>, com objetivo de realizar ensaios mecânicos de materiais. 
A versão atual (v0.1) inclui ferramentas para a realização de ensaios de tração, compressão, flexão e cisalhamento, 
bem como a emissão de relatórios por ensaio ou por análise 
comparativa entre dois ou mais para suportar os estudos do cliente.
<body>

---

## 🚀 Sobre o código

A obtenção de cópias do projeto para fins de desenvolvimento e teste são extritamente proibidos, salvo com o 
consentimento do seu desenvolvedor <a href=“https://github.com/Brunosalata“>Brunosalata</a>

### 📋 Pré-requisitos

A comunicação serial acontece entre aplicação e placa de automação no interior do equipamento, sendo impossível
desfrutar de todas as funcionalidades da aplicação, visto que os dados gerados pelo ensaio acontecem no equipamento.
Com isso, a plena utilização desse projeto implica na aquisição de um dos equipamentos Biopdi
<span style="font-size:10px; vertical-align: super"> ®</span>. Fora isso, o sistema deve possuir memória mínima de 4 gb
e ao menos dois núcleos de processamento, para gerantir melhor experiência do usuário.


### 🔧 Aplicação

Para criar o ambiente necessário para sua utilização, será necessário executar a aplicação em 
computador que atenda aos requisitos mínimos do sistema e esteja conectado via porta USB ao
equipamento onde será realizado o ensaio.

Instalação:

```
Executar o arquivo .exe, disponibilizado pela equipe de atendimento ou seu desenvolvedor
```

Preparo do equipamento:

```
1 - Com o equipamento bem alocado, encaixe as garras referentes ao ensaio de interesse
2 - Plugue o cabo de energia na tomada e ative o botão de ligar
3 - Execute a aplicação e faça o login (ou acesso rápido)
4 - Verifique a conexão serial na aba 'Ensaio'
5 - Preencha os campos de parâmetros do ensaio segundo protocolo desejado
6 - Inicie o ensaio
7 - Acesse os dados gerados na aba Relatório
8 - Análises estatísticas de um grupo de ensaios podem ser feitas na aba Dashboard
9 - Os relatórios poderão ser impressoas ou armazenados nos formatos (PDF, CSV, DOCX, XML entre outros)
```

Boas práticas na utilização da aplicação garantem resultados de melhor qualidade.

## ⚙️ Testes automatizados

Até o momento não foram inseridos testes automatizados no sistema.

### 🔩 Testes unitários e validações

Inserir testes e motivos.

```
Exemplos
```

## 📦 Implantação

A implantação será realizada pela equipe técnica, que fornecerá todas as informações pertinentes.

## 🛠️ Desenvolvimento

Para o desenvolvimento do projeto foram adotados:

* [IntelliJ IDE](https://www.jetbrains.com/pt-br/idea/) - Para a codificação em Java
* [Scene Builder](https://gluonhq.com/products/scene-builder/) - Criação da interface gráfica
* [Jaspersoft Studio](https://community.jaspersoft.com/) - Criação do layout dos relatórios
* [SQLite Studio](https://sqlitestudio.pl/) - Gerenciamento dos dados armazenados

Ferramentas inseridas no projeto:

* [JavaFX](https://openjfx.io/) - O framework web usado
* [Maven](https://maven.apache.org/) - Gerenciamento de Dependências
* [Xerial](https://xerial.org/software/) - Driver JDBC para utilização do banco de dados SQLite em Java
* [JSerialComm](https://fazecast.github.io/jSerialComm/) - Biblioteca para comunicação serial
* [Apache POI](https://poi.apache.org/) - API para manipulação de arquivos Office
* [Barbecue](https://barbecue.sourceforge.net/) - Biblioteca paraa geração de códigos de barra
* [jrviewer-fx](https://github.com/hawkxu/jrviewer-fx) - Visualização da previa do relatório na interface
* [JFXtras](https://jfxtras.org/) - Biblioteca permite manipulação de calendário em elementos visuais
* [itext](https://itextpdf.com/) - Geração e manipulação de PDF
* [Jasper Report](https://community.jaspersoft.com/) - Criação e emissão de relatórios
* [Apache Xerces](https://xerces.apache.org/xerces2-j/) - Analisar, validar, serializar e manipular XML
* [Apache Commons Logging](https://commons.apache.org/proper/commons-logging/) - Implementações de log e 
implementações de wrapper para Jssper Report
* [Apache Commons Collections](https://commons.apache.org/proper/commons-collections/) - Trabalhar com Collections 
no Jasper Report
* [Apache Commons Digester](https://commons.apache.org/proper/commons-digester/) - permite configurar um módulo de 
mapeamento de objetos XML -> Java
* [Apache Commens BeanUtils](https://commons.apache.org/proper/commons-beanutils/) - Trabalhar com Collections
* [JUnit 5](https://junit.org/junit5/) - Realização de testes unitários (não implementado ainda)


## 📌 Versão

O controle de versão é feito via Git e GitHub. PAra acessá-las, observe as Tags presentes no projeto

## ✒️ Autor

* **Bruno Salata Lima** - *Projeto completo* - [Brunosalata](https://github.com/Brunosalata)


## 📄 Licença

Este projeto está sob a licença "Biopdi license" - Acesse [BiopdiLicense.md](https://github.com/Biopdi) para detalhes.

## 🎁 Agradecimentos

Um grande privilégio ter tido a oportunidade de desenvolver, e estar desenvolvendo, este projeto. Meu primeiro projeto
profissional como desenvolvedor Java e tenho muito orgulho de escrever que cada linha inserida foi meticulosamente 
pensada. O conhecimento adquirido nesse processo não tem preço e depois de 34 anos, finalmente descobri meu lugar.</br>
Obrigado, Biopdi<span style="font-size:10px; vertical-align: super"> ®</span>, por me permitirem isso.



---
</body>

<footer> Por [Bruno Salata Lima](https://gist.github.com/Brunosalata) </footer>
