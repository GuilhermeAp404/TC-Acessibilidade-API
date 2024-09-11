# Sobre a aplicação

Esta aplicação é destinada a auxiliar pessoas com deficiência visual, utilizando a API do Google Cloud Vision para detectar e reconhecer textos em rótulos e embalagens. A API retorna os nomes das marcas e tipos de produtos, proporcionando uma experiência mais acessível e inclusiva.

## Tecnologias utilizadas
<p align="center">
	<a href="https://skillicons.dev">
		<img src="https://skillicons.dev/icons?i=java,spring,gcp,idea,postman" />
	</a>
</p>

## Funcionalidades

Essa aplicação possui uma rota capaz de receber uma imagem e retornar o texto contido nela.

```http
  POST /api/vision 
```

#### Recebe
Parametro   | Descrição
--------- | ------
image | Recebe um arquivo de imagem .jpg ou .png no corpo da requisição.

#### Retorna
Parametro   | Descrição
--------- | ------
products | Array de multiplas identificações do produto.



