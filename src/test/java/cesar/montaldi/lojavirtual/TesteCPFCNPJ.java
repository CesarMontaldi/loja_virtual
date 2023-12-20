package cesar.montaldi.lojavirtual;

import cesar.montaldi.lojavirtual.util.ValidaCNPJ;
import cesar.montaldi.lojavirtual.util.ValidaCPF;

public class TesteCPFCNPJ {
	
	public static void main(String[] args) {
		
		boolean isCNPJ = ValidaCNPJ.isCNPJ("53.569.670/0001-90");
		System.out.println("CNPJ válido: " + isCNPJ);
		
		boolean isCPF = ValidaCPF.isCPF("823.842.460-70");
		System.out.println("CPF válido: " + isCPF);
	}

}
