package br.com.marcell.brbrasilis;

/**
 * Created by Marcell on 04/04/2016.
 */
public class Passageiros {

    private int codigo;
    private int[] codigoPassageiros = new int[50];
    private String placaPassageiros;
    private String v;
    private String nome;
    private String documento;
    private String naturalidade;
    private String estadoCivil;
    private String telefone;
    private String escolaridade;
    private String endereco;
    private String ocupacao;
    private String cinto;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int[] getCodigoPassageiros() {
        return codigoPassageiros;
    }

    public void setCodigoPassageiros(int[] codigoPassageiros) {
        this.codigoPassageiros = codigoPassageiros;
    }

    public String getPlacaPassageiros() {
        return placaPassageiros;
    }

    public void setPlacaPassageiros(String placaPassageiros) {
        this.placaPassageiros = placaPassageiros;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(String ocupacao) {
        this.ocupacao = ocupacao;
    }

    public String getCinto() {
        return cinto;
    }

    public void setCinto(String cinto) {
        this.cinto = cinto;
    }
}
