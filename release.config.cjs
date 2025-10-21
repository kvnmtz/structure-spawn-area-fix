/**
 * @type {import('semantic-release').GlobalConfig}
 */
module.exports = {
  branches: ['main'],
  plugins: [
    '@semantic-release/commit-analyzer',
    '@semantic-release/release-notes-generator',
    './update-version.js',
    [
      '@semantic-release/git',
      {
        assets: ['gradle.properties'],
        message: 'chore(release): update version for ${nextRelease.version} [skip ci]',
      },
    ],
    [
      '@semantic-release/exec',
      {
        prepareCmd: './gradlew build',
      },
    ],
    [
      '@semantic-release/github',
      {
        'assets': [
          'build/libs/!(*-@(dev|sources|javadoc)).jar',
        ],
      },
    ],
    'semantic-release-export-data',
  ],
};
