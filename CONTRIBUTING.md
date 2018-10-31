# How to contribute

**Important**: Contact us at opensource@neotys.com for any question and before working on a modification.

To contribute, send us a pull request: 
1. Fork the repository.
2. In your fork, make your change in a branch that's based on this repo's master branch.
3. Commit the change to your fork, using a clear and descriptive commit message.
4. Create a pull request, answering any questions in the pull request form.

Before you send us a pull request, please be sure that:
1. You signed the [contributor's agreement](neotys-cla.pdf) and sent it to opensource@neotys.com. This will allow us to review and accept contributions. One agreement covers several GitHub repositories.
2. You're working from the latest source on the master branch.
3. You check existing open, and recently closed, pull requests to be sure that someone else hasn't already addressed the problem.
4. You create an issue before working on a contribution that will take a significant amount of your time.

For contributions that will take a significant amount of time, [open a new issue](https://github.com/Neotys-Labs/Script-Converter/issues/new) to pitch your idea before you get started. Explain the problem and describe the content you want to see added to the documentation. Let us know if you'll write it yourself or if you'd like us to help. We'll discuss your proposal with you and let you know whether we're likely to accept it. We don't want you to spend a lot of time on a contribution that might be outside the scope of the project or that's already in the works.

## Dependencies
The converter is based on several other components from the project [neoload-models](https://github.com/Neotys-Labs/neoload-models/)

| Steps | Project / Component |
|-------|---------------------|
| 1. Gather conversion settings / Entry point | [Script-Converter](https://github.com/Neotys-Labs/Script-Converter) |
| 2. Read Script into the data model | [neoload-models/models-readers/](https://github.com/Neotys-Labs/neoload-models/tree/master/models-readers) <br>[neoload-models/neoload-project/](https://github.com/Neotys-Labs/neoload-models/tree/master/neoload-project)
| 3. Write the data model to NeoLoad project | [neoload-models/neoload-writer/](https://github.com/Neotys-Labs/neoload-models/tree/master/neoload-writer)

