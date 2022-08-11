import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.parsePath
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.UnixFileSystemProvider
import io.github.excu101.filesystem.unix.path.UnixPath
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PathSegmentTest {

    @Before
    fun installFileSystem() {
        FileProvider.install(system = UnixFileSystem(provider = UnixFileSystemProvider()))
    }

    private val path = "/storage/emulated/0"
    private val separator = '/'

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getNames() = runTest {
        var cPath: Path = parsePath(input = path)
        val segments = mutableListOf<String>()

        while (true) {
            segments.add(cPath.getName().toString())
            cPath = cPath.parent ?: break
        }

        segments.forEach {
            println(it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun resolvePath() = runTest {
        var cPath: Path = parsePath(input = path)
        val newPath = FileProvider.parsePath(input = "Android")
        cPath = cPath.resolve(newPath)

        println(newPath.toString())
        println(cPath.toString())
    }

}