package virtuoel.discarnate.api;

@FunctionalInterface
public interface TriConsumer<T, U, V>
{
	void accept(T k, U v, V s);
}
