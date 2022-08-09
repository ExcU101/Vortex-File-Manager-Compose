import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.UnixFileSystemProvider
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.provider.AndroidFileProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProviderTest {

    @BeforeEach
    fun installFileSystem() {
        FileProvider.install(system = UnixFileSystem(provider = UnixFileSystemProvider()))
    }

    private val provider = mockk<AndroidFileProvider>()
    private val testPath = FileProvider.parsePath(input = "Path 100")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    suspend fun provideData() = runTest {
        val path = mockk<Path>()
        every { path } returns testPath
        coEvery { provider.provide(path) } returns listOfPath(10) {
            toPath(value = "$it")
        }.map { FileModel(it) }
    }

    private fun listOfPath(count: Int, block: (Int) -> Path): List<Path> {
        return List(size = count, init = block)
    }

    private fun toPath(value: String) = FileProvider.parsePath(value)

}