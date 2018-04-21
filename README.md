![Version](https://img.shields.io/badge/Version-1.0-blue.svg) ![Java Version](https://img.shields.io/badge/Java-%3E=1.8-red.svg)
# Via Cep Client!

Este é um client Java para consumo da [API ViaCEP](https://viacep.com.br/).
O client usa somente recursos nativos do Java, especialmente recursos do Java 8, por tanto, para uso do cliente é necessário está rodando um projeto Java 8+.

# Como usar
O client simplifica muito o processo de consumo da api, bastando apenas o programador instanciar a classe `ViaCepWebService` passando em seu construtor o CEP como uma **String** e **SEM MÁSCARA (apenas números)**. ( `new ViaCepWebService("01001000")` )
O ideal é que o CEP seja validado com antecedência pelo programador, antes de ser informado no construtor, para que não ocorra erros.
O cep também passa por validação pelo client.
Se nenhuma exceção for lançada, o endereço preenchido pode ser obtido através do método `getEnderecoViaCep()`.

# Exemplo de uso

    ViaCepWebService cepWebService = new ViaCepWebService("01001000");
    EnderecoViaCep enderecoViaCep = cepWebService.getEnderecoViaCep();
    System.out.println(enderecoViaCep.toString());
