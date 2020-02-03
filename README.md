# Android Template
Android template project made with Kotlin, Jetpack, MVVM, Dagger, and RxJava

# Setup fork updates against upstream
This adds the forked repo in your version control and is useful when you want to stay up to date with the latest changes. In the end, you decide what gets merged or not.

1. Add remote for the upstream repo
<br> `git remote add upstream git@git.wf.mk:project-templates/android-template.git`

2. Disable push to upstream (fetch only)
<br> `git remote set-url --push upstream DISABLE`

3. Verify
<br> `git remote -v`

    ```
    origin	git@github.com:webfactorymk/android-template-example.git (fetch)
    origin	git@github.com:webfactorymk/android-template-example.git (push)
    upstream	git@github.com:webfactorymk/android-template.git (fetch)
    upstream	DISABLE (push)
    ```


> You can also disable commits on upstream with pre-commit hook
>
> 1. go to your repository
> 2. create file .git/hooks/pre-commit with following content:
>  
>    ```
>    #!/bin/sh
>     branch="$(git rev-parse --abbrev-ref HEAD)"
>     if [ "$branch" = "upstream" ]; then
>        echo "Upstream is read only!"
>        exit 1
>     fi
>    ```
>    
>3. make it executable
>`chmod +x .git/hooks/pre-commit`
>
