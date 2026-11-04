package com.example.test

import com.example.Repo

@spike.Include
class FakeRepo : Repo {
    override fun name() = "fake"
}

@spike.test.Replaces(Repo::class)
val FakeRepo.asRepo: Repo get() = this
