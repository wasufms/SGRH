# SGRH
Sistema de Gerenciamento de Recursos Hídricos

Sistema desenvolvido como Implementação de Referência da arquitetura proposta na dissertação de mestrado de Wilson Alves da Silva, pela UFPE, sob o título:
Uma Arquitetura para Orquestração da Distribuição de Água no Semiárido Brasileiro Baseado em Internet das Coisas e Computação em Nuvem.

Este trabalho apresenta uma arquitetura baseado na Internet das Coisas e Computação em Nuvem que disponibiliza serviços para orquestrar a distribuição de água no Semiárido brasileiro.

Esta arquitetura provê meios para a realização da seguinte sequência de atividades: selecionar a carrada a ser entregue; geração do Token que autoriza e valida a entrega de água; monitoramento do deslocamento do Pipeiro; valdação da autorização para a entrega da água;
medição do volume e qualidade da água entregue; assinatura para confirmação da água recebida; e entrega do Pacote de Recebimento para a nuvem disponibilizando esses dados como um serviço.

Esta Implementação de Referência consiste dos seguintes módulos: 

Módulo Nó: 
Instalado na cisterna do Beneficiário e implementado em C. Sua principal função é assinar e verificar os
pacotes recebidos, bem como controlar os sensores usados para medir o volume e a pureza da água recebida.

Módulo Gateway: 
Instalado no smartphone do Pipeiro, implementado em Java para Android. A função principal deste módulo é realizar a
comunicação entre o Nó e o Context Broker, servindo como um gateway móvel.

Plataforma de mediação de dados IoT - FIWARE
Deve gozar das características de um ambiente de Computação em Nuvem como elasticidade, umbiquidade, escalabilidade, acesso amplo via rede e segurança. Nesta implementação foi usado o FIWARE. 

Módulo Proxy: 
Instalado na Plataforma IoT (FIWARE), implementado em Java e é responsável pela criação do Token para autorização da comunicação do Gateway com o Nó e verificação do Pacote de Recebimento.

Módulo Context Broker
Implementação do Padrão Arquitetural Publish/Subscribe. Instalado na Plataforma IoT (FIWARE) e é o responsável pela orquestração das mensagens trocadas com a AS e atualização do contexto da aplicação.