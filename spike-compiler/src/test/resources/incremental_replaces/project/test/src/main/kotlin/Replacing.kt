package com.example.test

import com.example.Repo

@spike.test.Replaces(Repo::class)
class FakeRepo : Repo {
    override fun name() = "fake"
}
