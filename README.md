# MLUtils

A collection of utilities I wrote for ML stuff!

## Features

- K-means clustering
- Principal component analysis
- Convolution
- 1D/2D FFT/IFFT

It also includes some code for compatibility between projects:

- [ndarray.simd](https://github.com/Martmists-GH/ndarray.simd)
  - This provides the primary data type used in this repo, `F64Array`.
  - Support was added to read/write `F64Array`s to/from CSV files.
- [Exposed](https://github.com/JetBrains/Exposed) and [pgGvector](https://github.com/pgvector/pgvector)
  - Supports storing `F64Array`s in exposed tables using the `PGvector` type, as well as implementing a DSL for all operations.
- [LangChain4J](https://github.com/langchain4j/langchain4j)
  - Supports converting `F64Array`s to/from LangChain4J's `Embedding` type.
- [Scrimage](https://github.com/sksamuel/scrimage)
  - Support was added to read/write `F64Array`s to/from image files.
  - Supports:
    - PNG
    - JPEG
    - WEBP (does require the `scrimage-webp` module)
- [Kotlinx DataFrame](https://github.com/Kotlin/dataframe)
  - Support was added to convert `F64Array`s to/from DataFrame's `DataColumn<Double>` type.

### Planned features

- [MultiK](https://github.com/Kotlin/multik) support
  - Potentially switch the entire codebase to use MultiK instead of Viktor at the core, this would also make it multiplatform.
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) support
  - Support for serializing/deserializing `F64Array`s using kotlinx.serialization.
- Built-in extensions for dealing with ND data (like colors, complex numbers, etc.)
    
## Installation

Coming soon:tm: to my Maven repo.

## Test data sources

The following is a list of assets from `src/test/resources` and their origins. 

`hw3-data.csv`: https://docs.google.com/uc?export=download&id=1CjR6Q6nMN_2pTJJietr07mRjEYYSWR7U
- This data was sourced from [this Medium article](https://medium.com/@devamsheth20/statistical-test-for-k-means-cluster-validation-in-python-using-sorted-similarity-matrix-1e644ab029c0).

`plush.png`: [@Totalatomic_](https://twitter.com/Totalatomic_) on Twitter.
- This is an image Totalatomic_ made for me (@Martmists-GH) personally back in 2021. Its use is permitted in this project and forks for the purpose of testing, and it is not to be used for any other purpose unless explicitly stated.

## License

This project is licensed under the [3-Clause BSD NON-AI License](https://github.com/non-ai-licenses/non-ai-licenses/blob/main/NON-AI-BSD3).

The TL;DR is: You may use the code in this project for AI/ML purposes (heck, that's what it's made for in the first place), but the source code may not be used as part of a **dataset** to train AI.
