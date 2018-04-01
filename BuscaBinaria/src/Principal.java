import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/**
 * 
 * @author Arthur
 * Retorna detalhes sobre o CEP informado
 */
public class Principal {

	
	static final String FILEPATH = "/home/aluno/workspace/cep_ordenado.dat";
	
	static final int TAMANHOLINHA = 300;
	
	public static void main (String [] args){
	
		Executar();
			
	}
	
	private static void Executar(){
		
		Scanner scanner = null;
		
		try{
			
			System.out.println("Digite o Cep:");
			scanner = new Scanner(System.in);
			String Cep = scanner.next();					
			
			int intCep = 0;
			if ( Cep.matches("\\d{8}") ){
				intCep = Integer.parseInt(Cep);
				
				RandomAccessFile f = new RandomAccessFile(FILEPATH, "r");		
				
				Endereco endereco = ObterEndereco(intCep, f, 0, f.length() / TAMANHOLINHA);
				
				if ( endereco != null ){
					
					System.out.println("Logradouro: " + endereco.getLogradouro());
					System.out.println("Bairro: " + endereco.getBairro());
					System.out.println("Cidade: " + endereco.getCidade());
					System.out.println("Estado: " + endereco.getEstado());
					System.out.println("Sigla: " + endereco.getSigla());
					System.out.println("");
					
					Executar();
				}else {
					System.out.println("Cep não encontrado!\n");
					Executar();
				}
				
			}else{
				System.out.println("Cep digitado não é válido!\n");
				Executar();
			}
			

			
		}catch(Exception e){
			System.out.println("Cep não encontrado!\n");
			Executar();
		}
		
		if (scanner != null){
			scanner.close();
		}
		
	}
	
	
	private static Endereco ObterEndereco(Integer Cep, RandomAccessFile f, int PosIni, long PosFim) {

		Endereco endereco = null;
		
		try{
			endereco = new Endereco();
			
			int PosMeio = (int) (PosIni + PosFim) / 2;
			f.seek(PosMeio * TAMANHOLINHA);
			
			endereco.leEndereco(f);
			
			int compara = Cep.compareTo(Integer.parseInt(endereco.getCep()));
			
			if (compara == 1){
				endereco = ObterEndereco(Cep, f, PosMeio, PosFim);
			}else if ( compara == -1 ){
				endereco = ObterEndereco(Cep, f, PosIni, PosMeio);
			}
			
		}catch (StackOverflowError e) {
			endereco = null;
		}catch (IOException e){
			System.out.println("Arquivo não encontrado!\n");
			endereco = null;
		}

		return endereco;
	}
}
