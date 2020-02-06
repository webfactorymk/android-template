# Android Template
Android template project made with Kotlin, Jetpack, MVVM, Dagger, and RxJava

# Fork

## Setup fork updates against upstream
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

#### You can also disable commits on upstream with pre-commit hook

>> 1. go to your repository
>> 2. create file .git/hooks/pre-commit with following content:
>>  
>>    ```
>>    #!/bin/sh
>>     branch="$(git rev-parse --abbrev-ref HEAD)"
>>     if [ "$branch" = "upstream" ]; then
>>        echo "Upstream is read only!"
>>        exit 1
>>     fi
>>    ```
>>    
>> 3. make it executable
>> `chmod +x .git/hooks/pre-commit`

# License

    MIT License
    
    Copyright (c) 2020 Web Factory LLC
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
